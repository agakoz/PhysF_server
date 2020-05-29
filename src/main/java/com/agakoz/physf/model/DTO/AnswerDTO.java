package com.agakoz.physf.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class AnswerDTO {
    @NonNull
    int id;
    @NonNull
    private int patientId;
    @NonNull
    private int questionId;
    private String content;
}
