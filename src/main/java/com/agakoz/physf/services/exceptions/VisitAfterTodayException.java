package com.agakoz.physf.services.exceptions;

public class VisitAfterTodayException extends IllegalArgumentException {
    public VisitAfterTodayException(){
        super("You cannot create a patient file for future");
    }
}
