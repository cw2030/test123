package encrypt.md5;

import java.security.MessageDigest;

import org.apache.commons.net.util.Base64;

public class MD5 {
    
    static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
                                '9', 'a', 'b', 'c', 'd', 'e', 'f' }; 

    public byte[] md5(String content,String encode)throws Exception{
        MessageDigest md = MessageDigest.getInstance("MD5");
        System.out.println(md.getDigestLength());
        md.update(content.getBytes(encode));
        return md.digest();
    }
    
    public String md5toHex(String content,String encode)throws Exception{
        return byteToHexString(md5(content,encode));
    }
    
    public static String byteToHexString(byte[] tmp) {
        String s;
        // 用字节表示就是 16 个字节
        char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
        // 所以表示成 16 进制需要 32 个字符
        int k = 0; // 表示转换结果中对应的字符位置
        for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
            // 转换成 16 进制字符的转换
            byte byte0 = tmp[i]; // 取第 i 个字节
            str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换, 
            // >>> 为逻辑右移，将符号位一起右移
            str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
        }
        s = new String(str); // 换后的结果转换为字符串
        return s;
    }
    
    private void compare(String[] strs, int i,int j, int num) {
        //判断2个字符串谁的长度最小，则以当前长度作为num+1的最大标准
        if (strs[i].length()>=strs[j].length()) {
            if (num+1<=strs[j].length()) {
                if (strs[j].charAt(num)>strs[i].charAt(num)) {
                    String temp=strs[i];
                    strs[i]=strs[j];
                    strs[j]=temp;
                    //若相等，则判断第二个
                }else if(strs[j].charAt(num)==strs[i].charAt(num)){
                    num++;
                    compare(strs, i,j, num);
                }
            }
        }else{
            if (num+1<=strs[i].length()) {
                if (strs[j].charAt(num)>strs[i].charAt(num)) {
                    String temp=strs[i];
                    strs[i]=strs[j];
                    strs[j]=temp;
                    //若相等，则判断第二个
                }else if(strs[j].charAt(num)==strs[i].charAt(num)){
                    num++;
                    compare(strs, i,j, num);
                }
            }else{
                //表示当前字符串内容都一致，strs[j]的长度大。 则放前面。
                String temp=strs[i];
                strs[i]=strs[j];
                strs[j]=temp;
            }
        }
    }
}
