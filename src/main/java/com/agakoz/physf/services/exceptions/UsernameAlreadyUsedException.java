package com.agakoz.physf.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "The username is already used")
public class UsernameAlreadyUsedException extends IllegalArgumentException{

    private static final String EXCEPTION_MESSAGE = "Username \"%s\" is already used.";
    public UsernameAlreadyUsedException(String username){
        super(String.format(EXCEPTION_MESSAGE, username));
    }
}
