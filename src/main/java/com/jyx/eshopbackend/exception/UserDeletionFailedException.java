package com.jyx.eshopbackend.exception;

public class UserRemovalFailException extends Exception{
    public UserRemovalFailException() {
    }

    public UserRemovalFailException(String message) {
        super(message);
    }

    public UserRemovalFailException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserRemovalFailException(Throwable cause) {
        super(cause);
    }

    public UserRemovalFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
