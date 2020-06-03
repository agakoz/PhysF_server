package com.agakoz.physf.services.exceptions;

public class CurrentUserException extends RuntimeException {
    private static final String exceptionMsg = "Current user problem";

    public CurrentUserException() {
        super(exceptionMsg);
    }
}
