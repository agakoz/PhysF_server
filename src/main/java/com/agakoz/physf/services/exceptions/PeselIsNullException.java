package com.agakoz.physf.services.exceptions;

public class PeselIsNullException extends IllegalArgumentException{
    public PeselIsNullException(){
        super("pesel cannot be null");
    }
}
