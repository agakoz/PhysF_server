package com.agakoz.physf.services.exceptions;

public class NullEndTimeException extends IllegalArgumentException {
    public NullEndTimeException(){
        super("No end time");
    }
}
