package com.agakoz.physf.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class PatientCreateOrUpdateDTO {
    @NonNull
    private String name;
    @NonNull
    private String surname;
    @NonNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
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
