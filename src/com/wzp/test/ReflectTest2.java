package com.wzp.test;

public class ReflectTest2 {

	public void t()throws Exception{
		System.out.println("CF");
		if(System.currentTimeMillis() > 0)
			throw new NullPointerException("abcd");
		System.out.println("ef");
	}
}
