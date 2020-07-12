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
public class UserCreateDTO {

    @NonNull
    private String username;
    @NonNull
    private String password;
    private String name;
    private String surname;
    private String company;
    private String address;
    private String city;
    private String licenceNumber;
    private String specializations;
//    String professionalTitle;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private String email;

}

