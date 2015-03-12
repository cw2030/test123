package com.wzp.test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReflectTest {

	public static void main(String[] args) {
		ExecutorService service = Executors.newFixedThreadPool(3);
		service.submit(new T());
		service.submit(new T());
		service.submit(new T());
		service.submit(new T());
		service.submit(new T());
		service.submit(new T());
		
		Method[] methods = ReflectTest2.class.getMethods();
		ReflectTest2 rt = new ReflectTest2();
		for(Method m : methods){
			if(m.getName().equals("t")){
				try {
					m.invoke(rt);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	static class T implements Runnable{

		@Override
		public void run() {
			System.out.println(toString());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
}
