package xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class XmlMain {

    public static void main(String[] args) {
        try{
            SAXReader sax = new SAXReader();  
            Document xmlDoc = sax.read(XmlMain.class.getClassLoader().getResourceAsStream("test-spring-config.xml"));  
            Element root = xmlDoc.getRootElement();
            
            Element element = root.addElement("bean");
            element.addAttribute("class", "spring.DynamicBeanReaderImpl");
            
            Element element1 = root.addElement(new QName("dubbo:protocol"));
            element1.addAttribute("name", "dubbo");
            element1.addAttribute("port", "20880");
            
            FileOutputStream fos = new FileOutputStream("d:\\test.xml");
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            XMLWriter writer = new XMLWriter(fos);
            writer.write(xmlDoc);
            writer.close();
//            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
//            outputStream.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        

    }

}
