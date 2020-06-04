package com.agakoz.physf.services.exceptions;

import java.io.IOException;

public class NoUsersException extends IOException {
    public NoUsersException(){
        super("No users.");
    }
}
