package com.wzp;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

public class HdfsFileReader {

    public static void main(String[] args) throws Exception{
        Path dstDir = new Path("hdfs://10.0.30.67:8020/hbec/logs/account1/access.log.tar.gz");  
//      String file = "hdfs://10.0.30.67:9000/hbec/logs/account1/apache-flume-1.4.0-bin.tar.gz";
        
        Configuration conf = new Configuration();
//      FileSystem fs=FileSystem.get(conf);
        
        FileSystem fs=dstDir.getFileSystem(conf);
        System.out.println(fs);
        FSDataInputStream is=fs.open(dstDir);
        IOUtils.copyBytes(is, new FileOutputStream(new File("e://a4.tar.gz")), conf, true);
//      IOUtils.copyBytes(is, new FileOutputStream(new File("e://a.zip")), conf, true);   
//        System.out.println();


    }

}
