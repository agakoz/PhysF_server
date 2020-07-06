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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate birthDate;
    String email;

}


