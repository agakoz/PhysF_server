package com.agakoz.physf.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class QuestionGroupDTO {
    @NonNull
    int id;
    @NonNull
    private int userId;
    @NonNull
    private String name;
}
