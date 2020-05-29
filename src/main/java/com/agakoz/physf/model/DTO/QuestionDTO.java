package com.agakoz.physf.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class QuestionDTO {
    @NonNull
    int id;
    @NonNull
    private int questionGroupId;
    @NonNull
    private String content;
}
