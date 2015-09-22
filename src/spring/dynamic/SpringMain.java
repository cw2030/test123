package spring.dynamic;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringMain {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        SpringMain dbr = context.getBean(SpringMain.class);
        String[] names = context.getBeanDefinitionNames();
        for (String string : names) {
            System.out.println(string);
        }
                
        System.out.println(context.getId());
        System.out.println(dbr);
    }

}
