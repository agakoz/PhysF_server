package com.agakoz.physf.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "questions")
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Question {
    @Id
    @Column
    int id;

    @ManyToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)
    private QuestionGroup questionGroup;

    @Column
    @NonNull
    private String content;
}
