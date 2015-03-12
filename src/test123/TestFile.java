package test123;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;

public class TestFile {

	public static void main(String[] args) throws Exception{
		System.out.println("Default Charset=" + Charset.defaultCharset());  
		System.out.println("file.encoding=" + System.getProperty("file.encoding"));  
//		System.out.println("Default Charset in Use=" + FileTools.getDefaultCharSet());
		System.out.println("Default Charset=" + Charset.defaultCharset());  
		System.out.println("file.encoding=" + System.getProperty("file.encoding"));  
	}

}
