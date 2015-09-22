package spring.dynamic;

import java.io.ByteArrayOutputStream;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import xml.XmlMain;

public class MyBeanProcessor implements BeanFactoryPostProcessor {

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
            throws BeansException {
        System.out.println("MyBeanProcessor start");
        this.beanFactory = beanFactory;

        Resource ir = new ByteArrayResource(getInputXmlStream());

        XmlBeanDefinitionReader definitionReader = new XmlBeanDefinitionReader((BeanDefinitionRegistry) this.beanFactory);

        
        definitionReader.loadBeanDefinitions(ir);

        System.out.println("MyBeanProcessor start");
    }
    
    private byte[] getInputXmlStream(){
        try{
            SAXReader sax = new SAXReader();  
            Document xmlDoc = sax.read(XmlMain.class.getClassLoader().getResourceAsStream("test-spring-config.xml"));  
            Element root = xmlDoc.getRootElement();
            
            Element element = root.addElement("bean");
            element.addAttribute("class", "spring.SpringMain");
            
            Element element1 = root.addElement(new QName("dubbo:protocol"));
            element1.addAttribute("name", "dubbo");
            element1.addAttribute("port", "20880");
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            XMLWriter writer = new XMLWriter(outputStream);
            writer.write(xmlDoc);
            writer.close();
            byte[] b = outputStream.toByteArray();
            outputStream.close();
            return b;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
