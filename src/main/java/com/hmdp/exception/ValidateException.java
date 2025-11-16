package com.hmdp.exception;

/**
 * @author tankaiwen
 */
public class ValidateException extends BaseException {
    public ValidateException(String message) {
        super(message);
    }

    public ValidateException(String message, Throwable cause) {
        super(message, cause);
    }
}
