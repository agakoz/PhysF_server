package com.agakoz.physf.services.exceptions;

public class UsernameAlreadyUsedException extends IllegalArgumentException{
    public UsernameAlreadyUsedException(String username){
        super(String.format("Username \"%s\"is already used.", username));
    }
}
