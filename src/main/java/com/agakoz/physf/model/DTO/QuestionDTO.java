package com.agakoz.physf.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class QuestionDTO {
    int id;
    private int questionGroupId;
    private String content;
}
