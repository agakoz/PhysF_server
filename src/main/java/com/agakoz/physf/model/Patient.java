package com.agakoz.physf.model;

import lombok.*;
import org.hibernate.annotations.GeneratorType;
import org.hibernate.engine.jdbc.Size;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "patients")
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @NonNull
    int id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column
    @NonNull
    private String name;
    @Column
    @NonNull
    private String surname;
    @Column(name="birth_date")
    @NonNull
    private Date birthDate;
    @Column(length = 12)
    @NonNull
    private String pesel;
    @Column(length = 1)
    @NonNull
    private char sex;
    @Column
    @NonNull
    private String address;
    @Column(length = 50)
    @NonNull
    private String city;
    @Column(length = 320)
    private String email;
    @Column(length = 9)
    private String phone;
    @Column
    private String lifestyle;
    @Column
    private String profession;
    @Column
    private String guardian;
    @Column(name = "past_diseases")
    private String pastDiseases;
    @Column(name = "chronic_diseases")
    private String chronicDiseases;
    @Column
    private String hospitalization;
    @Column
    private String surgeries;
    @Column(name = "past_treatment")
    private String pastTreatment;
    @Column
    private String allergies;
    @Column(name = "family_diseases")
    private String familyDiseases;
    @Column(name = "medical_certificate")
    private String medicalCertificate;
    @Column(name = "extra_details")
    private String extraDetails;

}
