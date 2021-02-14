package com.agakoz.physf.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreatmentCycleInfoDTO {
    private int id;
    private int patientId;
    private String title;
    private String description;
    private String bodyPart;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("injuryDate")
    private LocalDate injuryDate;
    private String symptoms;
    private String examinationDesc;
    private String diagnosis;
    private String treatment;
    private String recommendations;
    private String notes;
    private String similarPastProblems;
}
