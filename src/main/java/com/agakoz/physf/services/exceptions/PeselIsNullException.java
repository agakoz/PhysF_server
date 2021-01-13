package com.agakoz.physf.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Brak numeru pesel")
public class PeselIsNullException extends IllegalArgumentException{
    public PeselIsNullException(){
        super("pesel cannot be null");
    }
}
