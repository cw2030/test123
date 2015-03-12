package com.wzp.test2;

import com.wzp.common.ClassUtil;
import com.wzp.common.ILoggerService;
import com.wzp.common.LoggerService;

public class TestLog2 {
    private static ILoggerService log = LoggerService.getLog(TestLog2.class);
    
    public TestLog2(){
        ClassUtil.getClasses("com.wzp");
    }
    public static void main(String[] args) {
        log.debug("TestLog2");
    }
    
    public void t(){
        log.debug("I test logback on TestLog2.");
    }

}
