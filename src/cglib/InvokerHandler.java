package cglib;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

public class InvokerHandler implements MethodInterceptor{

    @Override
    public Object intercept(Object target, Method method, Object[] params, MethodProxy methodProxy)
            throws Throwable {
        System.out.println("proxy before");
        Object obj = methodProxy.invokeSuper(target, params);
        System.out.println("proxy after");
        return obj;
    }

}
