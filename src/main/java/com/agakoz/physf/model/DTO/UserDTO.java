package com.agakoz.physf.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UserDTO {
    int id;
    @NonNull
    private String username;
    @NonNull
    private String password;
    @NonNull
    private String role;
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


