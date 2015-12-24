package cglib;

public class TestProxy implements ITestProxy {

    @Override
    public String hello(String name) {
        return "hello " + name;
    }

}
