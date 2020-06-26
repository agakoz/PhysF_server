package com.agakoz.physf.services.exceptions;

public class NullPatientException extends RuntimeException {
    public NullPatientException(){
        super("No patient assigned for the visit");
    }
}
