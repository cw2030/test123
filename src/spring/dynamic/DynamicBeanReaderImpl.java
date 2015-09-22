package spring.dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

public class DynamicBeanReaderImpl implements ApplicationContextAware {
    private Logger LOG = LoggerFactory.getLogger(DynamicBeanReaderImpl.class);
    private ConfigurableApplicationContext applicationContext = null;    
    private XmlBeanDefinitionReader beanDefinitionReader;
    
    /*初始化方法*/  
    public void init(){  
        beanDefinitionReader = new XmlBeanDefinitionReader((BeanDefinitionRegistry)  
                applicationContext.getBeanFactory());    
        beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(applicationContext));    
    }  
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {    
        this.applicationContext = (ConfigurableApplicationContext)applicationContext;    
    }  
    
    public void loadBean(){   
//        beanDefinitionReader.loadBeanDefinitions(resource);
        /*long startTime = System.currentTimeMillis();  
        String beanName = dynamicBean.getBeanName();  
        if(applicationContext.containsBean(beanName)){  
            LOG.warn("bean【"+beanName+"】已经加载！");  
            return;  
        }  
        beanDefinitionReader.loadBeanDefinitions(new DynamicResource(dynamicBean));  
        LOG.info("初始化bean【"+dynamicBean.getBeanName()+"】耗时"+(System.currentTimeMillis()-startTime)+"毫秒。"); */ 
    }   

}
