package com.wzp.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.wzp.common.enums.HttpMethod;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface AppUriHandler {
    String[] uris();
    String view();
    HttpMethod method() default HttpMethod.ALL;
    int userLevel() default 0;
}
