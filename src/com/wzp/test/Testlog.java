package com.wzp.test;

import java.util.UUID;

import org.nutz.aop.AopCallback;
import org.nutz.aop.InterceptorChain;
import org.nutz.aop.interceptor.LoggingMethodInterceptor;
import org.nutz.aop.interceptor.TransactionInterceptor;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.AnnotationIocLoader;
import org.nutz.resource.Scans;

import com.wzp.common.ILoggerService;
import com.wzp.common.LoggerService;
import com.wzp.test2.TestLog2;

public class Testlog {

    private static ILoggerService logger = LoggerService.getLog(Testlog.class);
    public static void main(String[] args) {
        logger.debug("I test Logback on TestLog class DEBUG.");
        logger.info("I test Logback on TestLog class INFO.");
        logger.error("error is {}.", "format");
        logger.warn(UUID.randomUUID().toString());
        
        LoggingMethodInterceptor interceptor = new LoggingMethodInterceptor();
        TransactionInterceptor trans;
        AnnotationIocLoader aioc;
        InterceptorChain tc;
        Aop aop;
        Scans scan;
        AopCallback call;
        new TestLog2().t();
        logger.info("abc");
    }

}
