package com.agakoz.physf.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.sql.Time;
import java.util.Date;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class IncomingVisitDTO {
    @NonNull
    int id;
    @NonNull
    private int userId;
    @NonNull
    private int patientId;
    @NonNull
    Date date;
    @NonNull
    Time time;
    String note;
    @NonNull
    Boolean completed;
}
