package com.agakoz.physf.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Zła długość numeru pesel")
public class BadLengthPeselException extends IllegalArgumentException{
    public BadLengthPeselException(){
        super("pesel should consist of 11 digits");
    }
}
