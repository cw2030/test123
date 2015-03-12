package test123;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class PlatformProxy implements InvocationHandler {
	private final  Object prr;
	 private final Class<?> name;
	 
	 public PlatformProxy(Class<?> name, Object prr) {
	  this.prr = prr;
	  this.name = name;
	 }

	// @Override
	 public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
	  System.out.println("invoke: method=" + method.getName() + ", args=" + args);
	  return null;
	 }

}
