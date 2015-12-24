package spring.custom;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import spring.custom.impl.ITestClass;

public class Main {

    private static ApplicationContext context;

    public static void main(String[] args) {
        context = new ClassPathXmlApplicationContext("spring/custom/applicationContext2.xml");
        
        ITestClass tc = context.getBean(spring.custom.TestClass.class);

        tc.t();
    }

}
