package hbec.app.common.hdfs;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * HDFS分布式文件系统操作
 * @author user
 *
 */
public class HdfsUtil {

    /**
     * 根据指定的目录，返回该目录内的所有文件和目录
     * @param hdfsDir
     * @return
     * @throws Exception
     */
    public static List<String> getHDFSFileList(String hdfsDir) throws Exception {
        List<String> fileList = new ArrayList<>();
        if (hdfsDir == null || hdfsDir.trim().equals("")) {
            throw new Exception("参数hdfsDir为空");
        }
        if (!isHDFSResource(hdfsDir)) {
            return fileList;
        }
        Path dirPath = new Path(hdfsDir);
        Configuration conf = new Configuration();
        FileSystem fs = dirPath.getFileSystem(conf);
        if (!fs.isDirectory(dirPath)) {
            throw new Exception(hdfsDir + " is not a directory");
        }
        FileStatus[] files = fs.listStatus(dirPath);
        for (FileStatus fileStatus : files) {
            fileList.add(fileStatus.getPath().toUri().toString());
        }
        return fileList;
    }

    /**
     * 根据指定的文件，返回该文件在HDFS系统上的文件流
     * @param hdfsFile
     * @return
     * @throws Exception
     */
    public static FSDataInputStream getFileOutputStream(String hdfsFile)
            throws Exception {
        if (hdfsFile == null || hdfsFile.trim().equals("")) {
            throw new Exception("参数hdfsFile为空");
        }
        if (!isHDFSResource(hdfsFile)) {
            return null;
        }
        Path filePath = new Path(hdfsFile);
        Configuration conf = new Configuration();
        FileSystem fs = filePath.getFileSystem(conf);
        if (!fs.isFile(filePath)) {
            return null;
        }
        return fs.open(filePath);

    }

    /**
     * 判断该文件资源是否是HDFS文件类型
     * @param file
     * @return
     * @throws Exception
     */
    private static boolean isHDFSResource(String file) throws Exception {
        if (file.length() < 7) {
            throw new Exception("文件不是HDFS资源");
        }
        if (!file.substring(0, 7).toLowerCase().equals("hdfs://")) {
            return false;
        }
        return true;
    }
}
