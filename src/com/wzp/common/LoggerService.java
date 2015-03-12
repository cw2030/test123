package com.wzp.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

public class LoggerService implements ILoggerService {
    private Logger log;

    public static ILoggerService getLog(String name) {
        return new LoggerService(name);
    }

    public static ILoggerService getLog(Class<?> cls) {
        return new LoggerService(cls);
    }

    private LoggerService(String name) {
        log = LoggerFactory.getLogger(name);
    }

    private LoggerService(Class<?> cls) {
        log = LoggerFactory.getLogger(cls);
    }

    @Override
    public void debug(Object msg) {
        if (log.isDebugEnabled()) {
            log.debug(msg.toString());
        }
    }

    @Override
    public void debug(String format, Object... args) {
        String msg = null;
        if (log.isDebugEnabled()) {
            msg = MessageFormatter.arrayFormat(format, args).getMessage();
            log.debug(msg);
        }

    }

    @Override
    public void info(Object msg) {
        if (log.isInfoEnabled()) {
            log.info(msg.toString());
        }

    }

    @Override
    public void info(String format, Object... args) {
        String msg;
        if (log.isInfoEnabled()) {
            msg = MessageFormatter.arrayFormat(format, args).getMessage();
            log.info(msg);
        }

    }

    @Override
    public void warn(Object msg) {
        if (log.isWarnEnabled()) {
            log.info(msg.toString());
        }

    }

    @Override
    public void warn(String format, Object... args) {
        String msg;
        if (log.isWarnEnabled()) {
            msg = MessageFormatter.arrayFormat(format, args).getMessage();
            log.warn(msg);
        }

    }

    @Override
    public void error(Object msg) {
        if (log.isErrorEnabled()) {
            log.error(msg.toString());
        }

    }

    @Override
    public void error(String format, Object... args) {
        String msg = MessageFormatter.arrayFormat(format, args).getMessage();
        log.error(msg);

    }

    @Override
    public void error(String msg, Throwable e) {
        log.error(msg, e);

    }

}
