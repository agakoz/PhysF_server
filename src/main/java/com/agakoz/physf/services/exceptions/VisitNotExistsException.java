package com.agakoz.physf.services.exceptions;

public class VisitNotExistsException extends IllegalArgumentException{

    public VisitNotExistsException(){
        super(String.format("visit does not exist "));
    }

}
