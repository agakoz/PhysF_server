package com.agakoz.physf.model.DTO;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserAccountDTO {

    @NonNull
    private String username;
    @NonNull
    private String password;


}


