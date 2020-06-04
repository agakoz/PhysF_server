package com.agakoz.physf.services.exceptions;

public class UsernameTooShortException extends IllegalArgumentException{
    public UsernameTooShortException(){
        super("Username is too short.");
    }
}
