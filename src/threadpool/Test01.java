package threadpool;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class Test01 {

    @Before
    public void setUp() throws Exception {}

    @Test
    public void test() {
        TaskPoolManager<MyBean> tpm = new TaskPoolManager<Test01.MyBean>(10, 10000, "myTaskPool", new TaskPoolManager.ITask<MyBean>() {
            @Override
            public void process(MyBean myBean) {
                System.out.println(Thread.currentThread().getName()+ ">>>>" + myBean.f1 + myBean.f2);
                
            }});
        tpm.start();
        int c = 100;
        while(c > 0){
            MyBean bean = new MyBean();
            bean.f1 = c;
            bean.f2 = "f" + c;
            tpm.add(bean);
            c--;
        }
        tpm.blockUntilTerminal(100);
        tpm.shutdown();
    }
    
    class MyBean{
        int f1;
        String f2;
    }

}
