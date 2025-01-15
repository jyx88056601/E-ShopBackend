package com.jyx.eshopbackend.exception;

public class PasswordNotMatchException extends Exception {


    public PasswordNotMatchException() {
        super("Password does not match the required criteria.");
    }


    public PasswordNotMatchException(String message) {
        super(message);
    }


    public PasswordNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }


    public PasswordNotMatchException(Throwable cause) {
        super(cause);
    }
}
