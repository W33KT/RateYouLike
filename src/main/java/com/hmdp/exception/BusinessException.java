package com.hmdp.exception;

/**
 * @author tankaiwen
 */
public class BusinessException extends BaseException {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
