package javassist;

public class Javassist01 {

    public static void main(String[] args) throws Exception{
        //获取class定义的容器
        ClassPool pool = ClassPool.getDefault();

        //获取一个已经存在的类
        CtClass cc = pool.get(Test02.class.getName());
        
        //设置一个父类
        cc.setSuperclass(pool.get(TestParent.class.getName()));
        
        //将新类输出本地文件
        cc.writeFile("d:\\");
        
        
        
    }

}
