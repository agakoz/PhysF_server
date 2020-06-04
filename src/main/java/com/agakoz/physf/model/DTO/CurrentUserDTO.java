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
    String name;
    String surname;
    String company;
    String address;
    String city;
    String licenceNumber;
    String specializations;
    String professionalTitle;
    Date birthDate;
    String email;

}


