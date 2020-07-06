package com.agakoz.physf.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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
    private   Date birthDate;
    private   String email;
    private boolean activated;
}


