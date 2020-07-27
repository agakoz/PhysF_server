package com.agakoz.physf.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NoPatientsException extends IOException {
    public NoPatientsException(){
        super("User has no patients.");
    }
}
