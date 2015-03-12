package hbec.app.common.hdfs;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.io.IOUtils;

public class HDFSTest {

    public static String file = "hdfs://10.0.30.67:8020/hbec/logs/account1/access.log.tar.gz";
    public static String dir = "hdfs://10.0.30.67:8020/hbec/logs/account1";

    public static void main(String[] args) throws Exception {
        // 测试文件列表

        List<String> fileList = HdfsUtil.getHDFSFileList(dir);
        for (String fileString : fileList) {
            System.out.println("file --> " + fileString);
        }
        for (int i = 0; i < 1000; i++) {
            new HdfsRun().start();
        }

        FSDataInputStream input = HdfsUtil.getFileOutputStream(file);
        IOUtils.copyBytes(input, new FileOutputStream(new File("e://a5.tar.gz")), new Configuration(), true);
        input.close();
        System.out.println("finish");
    }

    static class HdfsRun extends Thread {

        @Override
        public void run() {
            try {
                List<String> fileList = HdfsUtil.getHDFSFileList(dir);
                for (String fileString : fileList) {
                    System.out.println(Thread.currentThread().getId() + "::file --> " + fileString);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
