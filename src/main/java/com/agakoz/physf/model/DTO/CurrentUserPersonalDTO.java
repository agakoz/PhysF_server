package com.agakoz.physf.model.DTO;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class CurrentUserPersonalDTO {
    @NonNull
    String name;
    @NonNull
    String surname;
    String company;
    String address;
    String city;
    @NonNull
    String licenceNumber;
    String specializations;
    String professionalTitle;
    @NonNull
    Date birthDate;
    String email;

}


