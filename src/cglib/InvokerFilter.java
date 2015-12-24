package cglib;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.CallbackFilter;

public class InvokerFilter implements CallbackFilter {

    @Override
    public int accept(Method method) {
        if(method.getName().equals("hello")){
            return 0;
        }
        return 1;
    }

}
