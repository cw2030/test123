package com.wzp.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nutz.resource.Scans;

public class ClassUtil {
    private static ILoggerService log = LoggerService.getLog(ClassUtil.class);
    public static List<Class<?>> getClasses(String pack) {
        Scans scan = Scans.me();
        List<Class<?>> clses = scan.scanPackage(pack);
        for (Class<?> class1 : clses) {
            log.debug(class1.getName());
        }
        
        return clses;
    }
    
    /**
     * 初始化AppComponent、AppService、AppUriHandler
     * 
     * @param clses
     */
    public void ioc(List<Class<?>> clses){
        Map<String,Class<?>> iocClasses = new HashMap<String,Class<?>>();
        //找出基于类的注声明解服务
        
        //找出基于方法的注解服务
        
        //找到基于字段的注解服务，IOC
        
    }

}
