package com.agakoz.physf.services.exceptions;

public class PatientWithIdNotExistsException extends IllegalArgumentException{

    public PatientWithIdNotExistsException(int patientId){
        super(String.format("patient with id: \"%d\" does not exist ", patientId));
    }

}
