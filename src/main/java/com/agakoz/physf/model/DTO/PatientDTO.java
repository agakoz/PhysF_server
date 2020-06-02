package com.agakoz.physf.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor

public class PatientDTO {
    private String name;
    private String surname;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date birthDate;
    private String pesel;
    private char sex;
    private String address;
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
