package com.agakoz.physf.services.exceptions;

public class PersonWithPeselAlreadyExistsException extends IllegalArgumentException{
    public PersonWithPeselAlreadyExistsException(String person){
        super(String.format("%s with this pesel already exists", person));
    }
}
