package com.agakoz.physf.services.exceptions;

public class PlannedVisitBeforeTodayException extends IllegalArgumentException {
    public PlannedVisitBeforeTodayException() {
        super("Visit cannot be planned for past");
    }
}
