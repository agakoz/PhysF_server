package com.agakoz.physf.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Current user problem")
public class UserException extends RuntimeException {
//    private static final String exceptionMsg = "Current user problem";

    public UserException(String msg) {
        super(msg);
    }
}
