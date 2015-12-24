package javassist;

import java.lang.reflect.Method;

public class Example01 {

    public static void main(String[] args) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        //创建一个新类
        CtClass cc = pool.makeClass("javassist.Point");
        CtField cf = new CtField(pool.get("java.lang.String"),"userName",cc);
        cf.setModifiers(Modifier.PRIVATE);
        cc.addMethod(CtNewMethod.setter("setUserName", cf));
        cc.addMethod(CtNewMethod.getter("getUserName", cf));
        
        CtMethod cm1 = new CtMethod(pool.get("java.lang.String"), "execute", new CtClass[]{pool.get("java.lang.String")}, cc);
        cm1.setModifiers(Modifier.PUBLIC);
        StringBuffer body = new StringBuffer();
        body.append("{");
        body.append("System.out.println($1);");
        body.append("return \"Hello \" + $1;");
        body.append("}");
        cm1.setBody(body.toString());
        cc.addMethod(cm1);
        
        Class<?> cls = cc.toClass();
        Object obj = cls.newInstance();
        Method m = cls.getMethod("execute" , new Class[]{String.class});
        System.out.println(m.invoke(obj, new Object[]{"Javassist!"}));
        
    }
}
