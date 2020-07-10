package com.agakoz.physf.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    int id;
    private String username;
    private String password;
    private String role;
    String name;
    String surname;
    String company;
    String address;
    String city;
    String licenceNumber;
    String specializations;
    String professionalTitle;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate birthDate;
    String email;
    private boolean activated = false;
    @Size(max = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @JsonIgnore
    private String resetKey;



}


