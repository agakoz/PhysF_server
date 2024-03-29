package com.agakoz.physf.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
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
    private String professionalTitle;
    private String licenceNumber;
    private String specializations;
    private String email;

}

