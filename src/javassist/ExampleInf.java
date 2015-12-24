package javassist;

import java.lang.reflect.Method;

public class ExampleInf {

    public static void main(String[] args) throws Exception{
        ClassPool pool = ClassPool.getDefault();
        CtClass ctClass = pool.makeClass("javassist.Test");
        Class<?> inf = ITest.class;
        CtClass infClass = pool.get(inf.getName());
        ctClass.setInterfaces(new CtClass[]{infClass});
        
        Method[] methods = inf.getMethods();
        
        for(Method m : methods){
            
//            CtMethod cm = new CtMethod(pool.get(m.getReturnType().getName()), m.getName(), parameters, declaring)
        }
        
        

    }

}
