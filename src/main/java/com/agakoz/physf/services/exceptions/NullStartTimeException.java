package com.agakoz.physf.services.exceptions;

public class NullStartTimeException extends IllegalArgumentException {
    public NullStartTimeException(){
        super("No start time");
    }

}
