package com.agakoz.physf.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Pacjent z podanym peselem już istnieje")
public class PersonWithPeselAlreadyExistsException extends IllegalArgumentException{
    public PersonWithPeselAlreadyExistsException(String person){
        super(String.format("%s with this pesel already exists", person));
    }
}
