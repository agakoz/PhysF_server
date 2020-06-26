package com.agakoz.physf.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitWithPhotosDTO {
    @NonNull
    private int id;
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
    List<PhotoDTO> photos;
}
