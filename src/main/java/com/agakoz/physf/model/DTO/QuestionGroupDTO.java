package com.agakoz.physf.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class QuestionGroupDTO {
    int id;
    private int userId;
    private String name;
}
