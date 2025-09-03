package com.hmdp.exception;

/**
 * @author tankaiwen
 */
public class SystemException extends BaseException {
    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
