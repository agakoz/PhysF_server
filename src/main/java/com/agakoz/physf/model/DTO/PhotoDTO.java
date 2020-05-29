package com.agakoz.physf.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class PhotoDTO {
    @NonNull
    int id;
    @NonNull
    private int visitId;
    @NonNull
    private byte[] photo;
    private String description;

}
