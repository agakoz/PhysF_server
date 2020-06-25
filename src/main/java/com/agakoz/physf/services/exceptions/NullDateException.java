package com.agakoz.physf.services.exceptions;

public class NullDateException extends IllegalArgumentException {
    public NullDateException(){
        super("Date is null");
    }
}
