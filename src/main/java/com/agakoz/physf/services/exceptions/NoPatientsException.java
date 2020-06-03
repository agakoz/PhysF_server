package com.agakoz.physf.services.exceptions;

import java.io.IOException;

public class NoPatientsException extends IOException {
    public NoPatientsException(){
        super("User has no patients.");
    }
}
