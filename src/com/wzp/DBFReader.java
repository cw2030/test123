package com.wzp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.nutz.json.bean.JsonObject;

import com.alibaba.fastjson.JSONObject;
import com.wzp.dbf.MarketSH;

/**
 * DBF 文件读取器
 * 
 * @author ld
 */
public class DBFReader {

    private static final byte HEAD_LOC = 0;
    private static final byte VERSION_LOC = HEAD_LOC, VERSION_LEN = 1;
    private static final byte UPDATE_LOC = VERSION_LOC + VERSION_LEN,
            UPDATE_LEN = 3;
    private static final byte COUNT_LOC = UPDATE_LOC + UPDATE_LEN,
            COUNT_LEN = 4;
    private static final byte DATAIDX_LOC = COUNT_LOC + COUNT_LEN,
            DATAIDX_LEN = 2;
    private static final byte LENGTH_LOC = DATAIDX_LOC + DATAIDX_LEN,
            LENGTH_LEN = 2;
    private static final byte RESERVED1_LOC = LENGTH_LOC + LENGTH_LEN,
            RESERVED1_LEN = 16;
    private static final byte TABLETAG_LOC = RESERVED1_LOC + RESERVED1_LEN,
            TABLETAG_LEN = 1;
    private static final byte PAGETAG_LOC = TABLETAG_LOC + TABLETAG_LEN,
            PAGETAG_LEN = 1;
    private static final byte RESERVED2_LOC = PAGETAG_LOC + PAGETAG_LEN,
            RESERVED2_LEN = 2;
    private static final byte HEAD_LEN = RESERVED2_LOC + RESERVED2_LEN;

    private static final byte FIELD_LOC = HEAD_LEN;

    private final File file;
    private final RandomAccessFile dbf;
    private final FileChannel fc;
    private final Charset charset;

    private int version;
    private byte[] update;
    private int count;
    private int dataidx;
    private int length;
    private int tabletag;
    private int pagetag;
    private Field[] fields;

    public static void main(String[] args) {
        try {
            String shFilePath = "C:\\Users\\user\\Desktop\\行情相关\\行情\\Show2003.dbf";
            String szFilePath = "C:\\Users\\user\\Desktop\\行情相关\\行情\\sjshq.dbf";
            DBFReader dbfreader_SH = new DBFReader(new File(szFilePath));
            List<JSONObject> shList = dbfreader_SH.getSHMarketData();
            for(JSONObject market : shList){
                System.out.println(JSONObject.toJSONString(market));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<JSONObject> getSHMarketData() {
        List<JSONObject> shList = new ArrayList<>();
        try {
            List<DBFReader.Record> list_sh = recordsJustDel();
            Field[] fields = getFields();
            int length = fields.length;

            for (DBFReader.Record reader : list_sh) {
                JSONObject json = new JSONObject();
                for (int i = 0; i < length; i++) {
                    json.put(fields[i].getName().toLowerCase(), reader.getString(i));
                    // System.out.print(fields[i].getName() +"[" +
                    // reader.getString(i) + "]:");
                }
                shList.add(json);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return shList;
    }

    public DBFReader(final File file) throws FileNotFoundException, IOException {
        this(file, Charset.forName("gbk"));
    }

    public DBFReader(final File file, final Charset charset) throws FileNotFoundException,
            IOException {

        this.dbf = new RandomAccessFile(file, "r");
        this.fc = dbf.getChannel();
        this.charset = charset;
        this.file = file;

        load(true);
    }

    /**
     * 获取此读取的所指向的文件对象
     * 
     * @return 文件对象
     */
    public File getFile() {
        return this.file;
    }

    /**
     * 重新读取文件，使此对象与文件内容保持一致
     * 
     * @param all
     *            指示是否将所有字段信息一同载入内存
     * @throws IOException
     *             读取文件失败
     */
    public void load(boolean all) throws IOException {
        loadHead();
        if (all)
            loadField();
    }

    /**
     * 装载文件头信息
     * 
     * @throws IOException
     *             读取文件失败
     */
    private void loadHead() throws IOException {

        ByteBuffer bb = loadData(HEAD_LOC, HEAD_LEN);
        orderBytes(bb);

        version = bb.get();
        update = new byte[UPDATE_LEN];
        bb.get(update);
        count = bb.getInt();
        dataidx = bb.getShort();
        length = bb.getShort();
        bb.position(bb.position() + RESERVED1_LEN);
        tabletag = bb.get();
        pagetag = bb.get();

        bb.clear();

    }

    /**
     * 装载字段信息
     * 
     * @throws IOException
     *             读取文件失败
     */
    private void loadField() throws IOException {

        ByteBuffer bb = loadData(FIELD_LOC, getFieldCount() * Field.LENGTH);
        orderBytes(bb);

        Field[] fds = new Field[getFieldCount()];
        for (int i = 0; i < fds.length; i++) {
            fds[i] = new Field(i, bb);
        }
        fields = fds;

        bb.clear();
    }

    /**
     * 将所有记录一次性载入内存
     * 
     * @throws IOException
     *             读取文件失败
     */
    private List<Record> loadRecordsJustDel() throws IOException {

        ByteBuffer bb = loadData(getDataIndex(), getCount() * getRecordLength());

        List<Record> rds = new ArrayList<Record>(getCount());
        for (int i = 0; i < getCount(); i++) {
            byte[] b = new byte[getRecordLength()];
            bb.get(b);

            // if((char)b[0] == '*')
            // {
            Record r = new Record(b);
            rds.add(r);
            // }
        }

        bb.clear();

        return rds;
    }

    private List<Record> loadRecordsWithOutDel() throws IOException {

        ByteBuffer bb = loadData(getDataIndex(), getCount() * getRecordLength());

        List<Record> rds = new ArrayList<Record>(getCount());
        for (int i = 0; i < getCount(); i++) {
            byte[] b = new byte[getRecordLength()];
            bb.get(b);

            if ((char) b[0] != '*') {
                Record r = new Record(b);
                rds.add(r);
            }
        }

        bb.clear();

        return rds;
    }

    private List<Record> loadRecords() throws IOException {

        ByteBuffer bb = loadData(getDataIndex(), getCount() * getRecordLength());

        List<Record> rds = new ArrayList<Record>(getCount());
        for (int i = 0; i < getCount(); i++) {
            byte[] b = new byte[getRecordLength()];
            bb.get(b);
            Record r = new Record(b);
            rds.add(r);

        }

        bb.clear();

        return rds;
    }

    private ByteBuffer loadData(int offset, int length) throws IOException {
        // return fc.map(MapMode.READ_ONLY, offset, length).load();

        ByteBuffer b = ByteBuffer.allocateDirect(length);
        fc.position(offset);
        fc.read(b);
        b.rewind();
        return b;

    }

    /**
     * 关闭文件
     * 
     * @throws IOException
     *             操作文件失败
     */
    public void close() throws IOException {
        fc.close();
        dbf.close();
    }

    /**
     * 获取处理字符型数据的字符集
     * 
     * @return 字符集
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * 获取版本号，分别有： 0x02 FoxBASE 0x03 FoxBASE+/dBASE III PLUS 无备注 0x30 Visual
     * FoxPro 0x43 dBASE IV SQL 表文件 无备注 0x63 dBASE IV SQL 系统文件 无备注 0x83
     * FoxBASE+/dBASE III PLUS 有备注 0x8B dBASE IV 有备注 0xCB dBASE IV SQL 表文件 有备注
     * 0xF5 FoxPro 2.x（或更早版本）有备注 0xFB FoxBASE
     * 
     * @return DBF 文件版本
     */
    public int getVersion() {
        return version;
    }

    /**
     * 获取最后更新日期
     * 
     * @return 更新日期，总是返回 3 个元素的数组，分别表示：0 年；1 月；2 日
     */
    public byte[] getUpdate() {
        return update;
    }

    /**
     * 获取记录总数
     * 
     * @return 记录总数
     */
    public int getCount() {
        return count;
    }

    /**
     * 获取记录数据的开始位置
     * 
     * @return 数据的开始位置的索引值
     */
    public int getDataIndex() {
        return dataidx;
    }

    /**
     * 获取每条记录的长度
     * 
     * @return 记录长度
     */
    public int getRecordLength() {
        return length;
    }

    /**
     * 获取表标记，分别有： 0x01 具有 .cdx 结构的文件 0x02 文件包含备注 0x04 文件是数据库 .dbc
     * 
     * @return 表标记
     */
    public int getTableTag() {
        return tabletag;
    }

    /**
     * 获取代码页标记
     * 
     * @return 代码页标记
     */
    public int getPageTag() {
        return pagetag;
    }

    /**
     * 获取字段的数量
     * 
     * @return 字段数量
     */
    public int getFieldCount() {
        if (fields == null)
            return (getDataIndex() - HEAD_LEN - 1) / Field.LENGTH;
        else
            return fields.length;
    }

    /**
     * 获取所有字段信息
     * 
     * @return 所有字段的信息
     */
    public Field[] getFields() {
        return fields;
    }

    /**
     * 获取指定索引处的数据记录，与 records 方法不同的是，如果该记录尚未载入内存则此方法将只读取文件中该记录的数据块
     * 
     * @param index
     *            记录索引
     * @return 索引处的数据记录
     * @throws IOException
     *             读取文件失败
     */
    public Record getRecord(int index) throws IOException {
        if (index < 0 || index >= getCount())
            throw new IndexOutOfBoundsException("index "
                                                + index
                                                + " out of bound "
                                                + getCount());
        int i = getDataIndex() + getRecordLength() * index;
        ByteBuffer b = loadData(i, getRecordLength());
        Record r = new Record(b);
        return r;
    }

    /**
     * 获取所有记录的迭代器(不包括标志位为删除的)，此方法将把所有记录一次性载入内存
     * 
     * @return 迭代器
     * @throws IOException
     *             读取文件失败
     */
    public List<Record> recordsWithOutDel() throws IOException {
        return loadRecordsWithOutDel();
    }

    /**
     * 获取所有记录的迭代器(不包括标志位为删除的)，此方法将把所有记录一次性载入内存
     * 
     * @return 迭代器
     * @throws IOException
     *             读取文件失败
     */
    public List<Record> recordsJustDel() throws IOException {
        return loadRecordsJustDel();
    }

    /**
     * 获取所有记录的迭代器，此方法将把所有记录一次性载入内存
     * 
     * @return 迭代器
     * @throws IOException
     *             读取文件失败
     */
    public List<Record> records() throws IOException {
        return loadRecords();
    }

    private ByteBuffer orderBytes(ByteBuffer bytes) {
        return bytes.order(ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * 字段属性
     */
    public class Field {

        public static final byte START_LOC = 0; // START_LEN = 1;
        public static final byte NAME_LOC = START_LOC, NAME_LEN = 11;
        public static final byte TYPE_LOC = NAME_LOC + NAME_LEN, TYPE_LEN = 1;
        public static final byte OFFSET_LOC = TYPE_LOC + TYPE_LEN,
                OFFSET_LEN = 4;
        public static final byte LENGTH_LOC = OFFSET_LOC + OFFSET_LEN,
                LENGTH_LEN = 1;
        public static final byte DECIMAL_LOC = LENGTH_LOC + LENGTH_LEN,
                DECIMAL_LEN = 1;
        public static final byte TAG_LOC = DECIMAL_LOC + DECIMAL_LEN,
                TAG_LEN = 1;
        public static final byte RESERVED_LOC = TAG_LOC + TAG_LEN,
                RESERVED_LEN = 13;
        public static final byte LENGTH = RESERVED_LOC + RESERVED_LEN;

        private final int index;
        private final String name;
        private final char type;
        private final int offset;
        private final int length;
        private final int decimal;
        private final int tag;

        public Field(final int index, final ByteBuffer src) {
            src.position(index * LENGTH);
            this.index = index;

            byte[] tb = new byte[NAME_LEN];
            src.get(tb);
            this.name = loadName(tb);

            this.type = (char) src.get();
            this.offset = src.getInt();
            this.length = src.get();
            this.decimal = src.get();
            this.tag = src.get();

        }

        private String loadName(byte[] src) {
            int i = NAME_LOC;
            for (; i < NAME_LOC + NAME_LEN; i++)
                if (src[i] == 0)
                    break;
            return new String(src, NAME_LOC, i, getCharset());
        }

        /**
         * 获取字段位置索引
         * 
         * @return 字段索引
         */
        public int getIndex() {
            return index;
        }

        /**
         * 获取字段的名称
         * 
         * @return 字段名称
         */
        public String getName() {
            return name;
        }

        /**
         * 获取字段类型，分别有： C-字符型 Y-货币型 N-数值型 F-浮点型 D-日期型 T-日期时间型 B-双精度型 I-整型 L-逻辑型
         * M-备注型 G-通用型 C-字符型（二进制） M-备注型（二进制） P-图片型
         * 
         * @return 字段类型
         */
        public char getType() {
            return type;
        }

        /**
         * 获取记录中该字段的偏移量
         * 
         * @return 偏移量
         */
        public int getOffset() {
            return offset;
        }

        /**
         * 获取该字段的长度（以字节为单位）
         * 
         * @return 字段的长度
         */
        public int getLength() {
            return length;
        }

        /**
         * 获取该字段的小数点位数
         * 
         * @return 小数点位数
         */
        public int getDecimal() {
            return decimal;
        }

        /**
         * 获取该字段的标记，分别是： 0x01 系统列（用户不可见） 0x02 可存储 null 值的列 0x04 二进制列（只适于字符型和备注型）
         * 
         * @return 字段的标记
         */
        public int getTag() {
            return tag;
        }
    }

    public class Record {

        private final byte[] src;

        public Record(final ByteBuffer src) {
            byte[] bs = new byte[src.capacity()];
            src.get(bs);
            this.src = bs;
        }

        public Record(final byte[] src) {
            this.src = src;
        }

        public String getString(final int index) {
            return getString(getFields()[index]).trim();
        }

        public String getString(final Field field) {
            return new String(src, field.getOffset(), field.getLength(), getCharset()).trim();
        }

        public int getInt(final int index) {
            return getInt(getFields()[index]);
        }

        public int getInt(final Field field) {
            String s = getString(field);
            return Integer.valueOf(s.trim());
        }

        public long getLong(final int index) {
            return getLong(getFields()[index]);
        }

        public long getLong(final Field field) {
            String s = getString(field);
            return Long.valueOf(s.trim());
        }

        public float getFloat(final int index) {
            return getFloat(getFields()[index]);
        }

        public float getFloat(final Field field) {
            String s = getString(field);
            return Float.valueOf(s);
        }

        public Object getObject(final int index) {
            return getObject(getFields()[index]);
        }

        public Object getObject(final Field field) {
            switch (field.getType()) {
            case 'C':
                return getString(field);
            case 'N':
                if (field.getDecimal() > 0)
                    return getFloat(field);
                else
                    return getLong(field);
            case 'I':
                return getInt(field);
            default:
                throw new UnsupportedOperationException("Unsupport type: "
                                                        + field.getType());
            }
        }

    }

}
