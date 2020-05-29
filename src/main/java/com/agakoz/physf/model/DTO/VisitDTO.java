package com.agakoz.physf.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitDTO {

    int id;
    @NonNull
    private int userId;
    @NonNull
    private int patientId;
    @NonNull
    Date date;
    String reason;
    String symptoms;
    String diagnosis;
    String recommendations;
    String examinationDesc;
    String treatment;
    String notes;
}
