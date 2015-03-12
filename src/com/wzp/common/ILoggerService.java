package com.wzp.common;

public interface ILoggerService {
    void debug(Object msg);
    void debug(String format, Object... args);

    void info(Object msg);
    void info(String format, Object... args); 

    void warn(Object msg);
    void warn(String format, Object... args); 

    void error(Object msg);
    void error(String format, Object... args); 
    void error(String msg, Throwable e);

}
