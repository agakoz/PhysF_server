package com.agakoz.physf.model.DTO;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {
    int id;
    @NonNull
    private String username;
    @NonNull
    private String password;
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


