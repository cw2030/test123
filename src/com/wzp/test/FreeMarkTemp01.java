package com.wzp.test;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;

import freemarker.cache.URLTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.Template;

public class FreeMarkTemp01 {

	public static void main(String[] args) throws Exception{
//		freemarker.log.Logger.selectLoggerLibrary(freemarker.log.Logger.LIBRARY_SLF4J);
		Configuration conf = new Configuration();
		conf.setTemplateLoader(new URLTemplateLoader() {
			@Override
			protected URL getURL(String name) {
				URL b = Thread.currentThread().getContextClassLoader().getResource("ftl/" + name);
				return b;
			}
		});
		
		conf.setDefaultEncoding("utf-8");
		//默认:ObjectWrapper.DEFAULT_WRAPPER
		//还可以手动设置是：ObjectWrapper.BEANS_WRAPPER
		//			  ObjectWrapper.SIMPLE_WRAPPER
		//也可以是自定义的任何实现ObjectWrapper接口的对象
		//可以不设置，默认入是ObjectWrapper.DEFAULT_WRAPPER
//		conf.setObjectWrapper(ObjectWrapper.DEFAULT_WRAPPER);

		Template tl = conf.getTemplate("ftl01.ftl");
		
		Bean1 d = new Bean1();
		d.a="aaa";
		d.b="bbb";
		HashMap<String,Object> data = new HashMap<>();
		data.put("data", d);
		Writer writer = new OutputStreamWriter(System.out);
		tl.process(data, writer);
		writer.flush();
		
	}

}
