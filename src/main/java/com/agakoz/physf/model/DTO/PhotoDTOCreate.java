package com.agakoz.physf.model.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
public class PhotoDTOCreate {
    @NonNull
    int id;
    @NonNull
    private int visitId;
    @NonNull
    private byte[] photo;
    private String description;

}
