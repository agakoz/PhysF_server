package com.agakoz.physf.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CurrentUserDTO {
    int id;
    @NonNull
    private String username;
    private String name;
    private   String surname;
    private   String company;
    private  String address;
    private  String city;
    private  String licenceNumber;
    private  String specializations;
    private  String professionalTitle;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private   LocalDate birthDate;
    private   String email;
    private boolean activated;
}


