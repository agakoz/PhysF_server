package com.agakoz.physf.services.exceptions;

import java.io.IOException;

public class NoVisitsException extends IOException {
    public NoVisitsException(){
        super("No visits to show");
    }
}
