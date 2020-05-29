package com.agakoz.physf.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PatientDTO {
    @NonNull
    int id;
    @NonNull
    private int userId;
    @NonNull
    private String name;
    @NonNull
    private String surname;
    @NonNull
    private Date birthDate;
    @NonNull
    private String pesel;
    @NonNull
    private char sex;
    @NonNull
    private String address;
    @NonNull
    private String city;
    private String email;
    private String phone;
    private String lifestyle;
    private String profession;
    private String guardian;
    private String pastDiseases;
    private String chronicDiseases;
    private String hospitalization;
    private String surgeries;
    private String pastTreatment;
    private String allergies;
    private String familyDiseases;
    private String medicalCertificate;
    private String extraDetails;
}
