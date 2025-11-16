package com.hmdp.aop;

import com.hmdp.dto.Result;
import com.hmdp.exception.BusinessException;
import com.hmdp.exception.SystemException;
import com.hmdp.exception.ValidateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ControllerExceptionAspect {
    /**
     * Pointcut: all methods under the controller package
     */
    @Pointcut("execution(* com.hmdp.controller..*(..))")
    public void controllerMethods() {}

    /**
     * Exception advice
     * @param ex
     * @return
     */
    @AfterThrowing(pointcut = "controllerMethods()", throwing = "ex")
    public Result handleException(Throwable ex) {
        if (ex instanceof ValidateException) {
            log.warn("ValidationException: {}", ex.getMessage());
            return Result.fail(StringUtils.isNotBlank(ex.getMessage()) ? ex.getMessage() : "Validation failed");
        } else if (ex instanceof BusinessException) {
            log.error("BusinessException: {}", ex.getMessage(), ex);
            return Result.fail(StringUtils.isNotBlank(ex.getMessage()) ? ex.getMessage() : "Business logic error");
        } else if (ex instanceof SystemException) {
            log.error("SystemException: {}", ex.getMessage(), ex);
            return Result.fail(StringUtils.isNotBlank(ex.getMessage()) ? ex.getMessage() : "System error, please retry or contact administrator");
        } else {
            log.error("Unknown Exception: {}", ex.getMessage(), ex);
            return Result.fail(StringUtils.isNotBlank(ex.getMessage()) ? ex.getMessage() : "Unknown error, please retry or contact administrator");
        }
    }
}

