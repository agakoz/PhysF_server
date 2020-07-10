package com.agakoz.physf.model.DTO;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {


    @NotBlank
    private String username;
    @NotBlank
    private String password;


}


