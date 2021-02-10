package com.agakoz.physf.model.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
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
    String email;
    private boolean activated = false;
    @Size(max = 20)
    @JsonIgnore
    private String activationKey;

    @Size(max = 20)
    @JsonIgnore
    private String resetKey;



}


