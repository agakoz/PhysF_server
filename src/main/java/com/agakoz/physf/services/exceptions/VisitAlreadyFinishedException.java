package com.agakoz.physf.services.exceptions;

public class VisitAlreadyFinishedException extends IllegalArgumentException {
    public VisitAlreadyFinishedException(String msg){
        super(msg);
    }
}
