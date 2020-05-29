package com.agakoz.physf.model;

import liquibase.pro.packaged.S;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "visits")
public class Visit {
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

    @NonNull
    @Column
    Date date;

    @Column
    String reason;

    @Column
    String symptoms;

    @Column
    String diagnosis;

    @Column
    String recommendations;

    @Column(name = "examination_desc")
    String examinationDesc;

    @Column
    String treatment;

    @Column
    String notes;

}
