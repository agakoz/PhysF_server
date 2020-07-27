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
    private String name;
    private String surname;
    private String company;
    private String address;
    private String city;
    private String licenceNumber;
    private String specializations;
    private String professionalTitle;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private String email;
    private boolean activated = false;
    @Size(max = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @JsonIgnore
    private String resetKey;


}


