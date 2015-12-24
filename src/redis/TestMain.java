package redis;

import java.lang.reflect.Field;

public class TestMain {

    public static void main(String[] args) {
        Field[] fs = SubClass.class.getDeclaredFields();
        for(Field f : fs){
            System.out.println(f.getName());
        }
        new SubClass().t();
    }

}
