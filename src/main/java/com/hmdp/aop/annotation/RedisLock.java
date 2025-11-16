package com.hmdp.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author tankaiwen
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisLock {
    /**
     * lock key
     */
    String key();

    /**
     * lock expire time
     */
    long timeout() default 10L;

    /**
     * time unit of expire time
     */
    TimeUnit unit() default TimeUnit.SECONDS;
}
