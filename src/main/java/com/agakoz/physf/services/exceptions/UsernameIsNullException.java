package com.agakoz.physf.services.exceptions;

public class UsernameIsNullException extends IllegalArgumentException{
    public UsernameIsNullException(){
        super("username cannot be null");
    }
}
