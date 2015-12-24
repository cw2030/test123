package javassist;

import java.lang.reflect.Method;

public class CreateProxyForInf {

    public static void main(String[] args) throws Exception{
        ClassPool pool = ClassPool.getDefault();
        //创建一个新的接口代理类
        CtClass cc = pool.makeClass("javassist.TestProxy");
        
        //获取接口类对象
        CtClass infCc = pool.get(ITest.class.getName());
        
        //设置代理接口
        cc.addInterface(infCc);
        
        //获取代理接口的方法
        CtMethod[] cms = infCc.getMethods();
        for(CtMethod cm : cms){
            CtMethod m = CtNewMethod.make(cm.getReturnType(), cm.getName(), cm.getParameterTypes(), cm.getExceptionTypes(), "return null;", cc);
            cc.addMethod(m);
        }
        cc.writeFile("d:\\");
    }
}
