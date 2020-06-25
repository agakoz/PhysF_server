package com.agakoz.physf.services.exceptions;

public class VisitWithIdNotExistsException extends IllegalArgumentException {
    public VisitWithIdNotExistsException(int visitId) {
        super(String.format("Visit with id: \"%d\" not exists.", visitId));
    }
}
