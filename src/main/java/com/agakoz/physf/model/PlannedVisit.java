package com.agakoz.physf.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "planned_visits")
public class PlannedVisit {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id", nullable = false)
    private Patient patient;


    @Column
    @NonNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate date;

    @Column(name = "start_time")
    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    @NonNull
    LocalTime startTime;

    @DateTimeFormat(pattern = "HH:mm")
    @JsonFormat(pattern = "HH:mm")
    @NonNull
    @Column(name = "end_time")
    LocalTime endTime;

    @Column
    String note;

}
