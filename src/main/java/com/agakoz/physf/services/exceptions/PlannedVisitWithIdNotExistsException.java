package com.agakoz.physf.services.exceptions;

public class PlannedVisitWithIdNotExistsException extends IllegalArgumentException {
    public PlannedVisitWithIdNotExistsException(int plannedVisitId) {

        super(String.format("Planned visit with id: \"%d\" not exists.", plannedVisitId));

    }
}
