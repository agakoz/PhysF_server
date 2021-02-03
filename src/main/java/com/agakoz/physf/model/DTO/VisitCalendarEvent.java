package com.agakoz.physf.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

@Data
@NoArgsConstructor
public class VisitCalendarEvent {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
//    private int treatmentCycleId;
    private String title;
    private int patientId;
//    private LocalDate birthDate;
//    private String patientName;
//    private String patientSurname;
    private boolean finished;

    public VisitCalendarEvent(
            int id,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
//            int treatmentCycleId,
            String treatmentCycleTitle,
            int patientId,
            LocalDate birthDate,
            String patientName,
            String patientSurname,
            boolean finished
    ) {
        this.id = id;
        this.start = LocalDateTime.of(date, startTime);
        this.end = LocalDateTime.of(date, endTime);
//        this.treatmentCycleId = treatmentCycleId;
        String cycleTitle = treatmentCycleTitle != null? treatmentCycleTitle : "nowa wizyta";
        this.title = patientSurname + " " + patientName + ": " + cycleTitle ;
        this.patientId = patientId;
//        this.birthDate = birthDate;
//        this.patientName = patientName;
//        this.patientSurname = patientSurname;
        this.finished = finished;
    }
}


