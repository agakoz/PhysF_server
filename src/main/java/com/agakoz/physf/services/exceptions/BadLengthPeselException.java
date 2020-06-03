package com.agakoz.physf.services.exceptions;

public class BadLengthPeselException extends IllegalArgumentException{
    public BadLengthPeselException(){
        super("pesel should consist of 11 digits");
    }
}
