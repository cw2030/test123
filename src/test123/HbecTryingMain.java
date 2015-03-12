package test123;

import java.lang.reflect.Proxy;

public class HbecTryingMain {

	public static void main(String[] args) {
		  Class<?> c = IDemoService.class;
		  Object pr = new Object();
		  IDemoService o = (IDemoService) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
		    new Class<?>[] { c }, new PlatformProxy(c, pr));
		  o.hello();
		 }

}
