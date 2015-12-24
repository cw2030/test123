package cglib;

import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.NoOp;

public class ProxyClass {

    public static void main(String[] args) {
        ITestProxy proxy = (ITestProxy)go(TestProxy.class);
        System.out.println(proxy.hello("change"));
    }
    
    private static Object go(Class<?> target){
        Enhancer proxy = new Enhancer();
        if(target.isInterface()){
            proxy.setInterfaces(new Class[]{target});
        }else{
            proxy.setSuperclass(target);
        }
        /**
         * NoOp
         */
        proxy.setCallbacks(new Callback[]{new InvokerHandler(),NoOp.INSTANCE});
        proxy.setCallbackFilter(new InvokerFilter());
        return proxy.create();
    }
    
}
