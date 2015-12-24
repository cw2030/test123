package javassist;

import java.lang.reflect.Method;

public class CreateNewClass {

    public static void main(String[] args) throws Exception{
        //获取class定义的容器
        ClassPool pool = ClassPool.getDefault();

        //创建一个新类
        CtClass cc = pool.makeClass("javassist.Test03");
        
        //创建字段
        CtField cf01 = CtField.make("private String name;",cc);
        cc.addField(cf01);
        
      //添加方法1
        CtMethod cm01 = CtNewMethod.make("public void setName(String name){this.name = name;}", cc);
        cc.addMethod(cm01);
        
        //添加方法2
        CtMethod cm02 = CtNewMethod.make("public void go(){System.out.println(\"hello:\" + name);}", cc);
        cc.addMethod(cm02);
//        cc.writeFile("d:\\");
        
        //实例对象
        Class<?> cls = cc.toClass();
        Object obj = cls.newInstance();
        Method m = cls.getMethod("setName", String.class);
        
        m.invoke(obj, "javassist test");
        
        m = cls.getMethod("go");
        m.invoke(obj);
    }

}
