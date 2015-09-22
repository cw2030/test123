package javassist;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Arrays;

import org.junit.Test;

import com.alibaba.dubbo.common.bytecode.Wrapper;

public class Test02 {

    @Test
    public void t1(){
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();  
        String name = runtime.getName(); 
        System.out.println(runtime.getBootClassPath());
        System.out.println(name);
        
        String[] methods = Wrapper.getWrapper(ITest.class).getMethodNames();
        System.out.println(Arrays.toString(methods));
        
    }
}
