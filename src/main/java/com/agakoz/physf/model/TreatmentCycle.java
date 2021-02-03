package com.agakoz.physf.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "treatment_cycles")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class TreatmentCycle {
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
    private String title;
    @Column
    private String description;
    @Column(name = "body_part")
    private String bodyPart;
    @Column(name = "injury_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate injuryDate;
    @Column
    private String symptoms;
    @Column(name = "examination_desc")
    private String examinationDesc;
    @Column
    private String diagnosis;
    @Column
    private String treatment;
    @Column
    private String recommendations;
    @Column
    private String notes;
    @Column
    private boolean archival = false;
    @Column(name = "similar_past_problems")
    private String similarPastProblems;
}
