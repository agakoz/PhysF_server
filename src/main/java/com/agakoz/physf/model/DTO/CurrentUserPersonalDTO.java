package com.agakoz.physf.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserPersonalDTO {
    String name;
    String surname;
    String company;
    String address;
    String city;
    String licenceNumber;
    String specializations;
    String professionalTitle;
    String email;

}


