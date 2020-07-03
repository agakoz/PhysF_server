package com.agakoz.physf.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import liquibase.pro.packaged.S;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "visits")
public class Visit {
//    visit info
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @NonNull
    private User user;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id", nullable = false)
    @NonNull
    private Patient patient;

    @Column
    @NonNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("date")
    private LocalDate date;

    @Column(name = "start_time")
    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    @NonNull
    private LocalTime startTime;

    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    @NonNull
    @Column(name = "end_time")
    private LocalTime endTime;

    @Column
    private String title;
// description

    @Column
    private String symptoms;

    @Column(name = "first_time")
    private  boolean firstTime;

//   examination & treatment
    @Column
    private String diagnosis;

    @Column
    private String recommendations;

    @Column(name = "examination_desc")
    private  String examinationDesc;

    @Column
    private String treatment;

    @Column
    private  String notes;

}
