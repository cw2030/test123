package cglib;

import java.lang.reflect.Method;
import java.util.Random;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

public class ProxyInterface {

    public static void main(String[] args) {
        Enhancer proxy = new Enhancer();
        proxy.setInterfaces(new Class[]{ITestProxy.class});
        proxy.setCallback(new MethodInterceptor() {
            
            @Override
            public Object intercept(Object arg0, Method arg1, Object[] arg2, MethodProxy arg3)
                    throws Throwable {
                System.out.println("hello proxy interface!");
                Random random = new Random();
                if(random.nextInt() % 2 == 0){
                    throw new MyException("haha");
                }
                return "Hello interface : " + arg2[0].toString();
            }
        });
        
        try{
            ITestProxy p = (ITestProxy)proxy.create();
            System.out.println(p.hello("上海"));
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        

    }

}
