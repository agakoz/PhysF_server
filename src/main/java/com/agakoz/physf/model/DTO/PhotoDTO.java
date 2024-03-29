package com.agakoz.physf.model.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDTO {
    @NonNull
    int id;
    @NonNull
    private byte[] photo;

}
