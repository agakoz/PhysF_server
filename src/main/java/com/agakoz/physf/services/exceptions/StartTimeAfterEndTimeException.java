package com.agakoz.physf.services.exceptions;

public class StartTimeAfterEndTimeException extends IllegalArgumentException {
    public  StartTimeAfterEndTimeException(){
        super("Start time cannot be after end time");
    }
}
