package javassist;

import java.lang.reflect.Method;

import javassist.CtField.Initializer;

import org.junit.Test;

public class Test01 {

    @Test
    public void t1(){
        ClassPool cp = ClassPool.getDefault();
        try{
            CtClass ctclass = cp.makeClass("bean.TestBean2");
            StringBuffer body = null;
            CtField ctField = new CtField(cp.get("java.lang.String"), "name", ctclass);
            ctField.setModifiers(Modifier.PRIVATE);
            
            ctclass.addMethod(CtNewMethod.setter("setName", ctField));
            ctclass.addMethod(CtNewMethod.setter("getName", ctField));
            ctclass.addField(ctField,Initializer.constant("default"));
            
            CtConstructor ctConstructor = new CtConstructor(new CtClass[]{}, ctclass);
            body = new StringBuffer();
            body.append("{\n name=\"me\"; \n}");
            ctConstructor.setBody(body.toString());
            ctclass.addConstructor(ctConstructor);
            
            CtMethod ctMethod = new CtMethod(CtClass.voidType, "execute", new CtClass[]{}, ctclass);
            ctMethod.setModifiers(Modifier.PUBLIC);
            body = new StringBuffer();
            body.append("{\n System.out.println(name);");
            body.append("\n System.out.println(\"execute ok\");");
            body.append("\n return ;");
            body.append("\n}");
            ctMethod.setBody(body.toString());
            
            
            
            ctclass.addMethod(ctMethod);
            Class<?> c = ctclass.toClass();
            Object o = c.newInstance();
            Method method = o.getClass().getMethod("execute", new Class[]{});
            method.invoke(o);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
}
