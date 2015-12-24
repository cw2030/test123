package spring.custom;

import spring.custom.impl.ITestClass;

public class TestClass implements ITestClass{

    @Override
    public boolean t() {
        System.out.println("Test t()");
        
        return false;
    }

}
