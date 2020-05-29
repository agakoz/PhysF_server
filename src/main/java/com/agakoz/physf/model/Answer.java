package com.agakoz.physf.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "answers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class Answer {
    @Id
    @Column
    int id;

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id", nullable = false)
    private Question question;

    @Column
    @NonNull
    private String content;
}
