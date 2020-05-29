package com.agakoz.physf.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "question_groups")
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class QuestionGroup {
    @Id
    @Column
    int id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Column
    @NonNull
    private String name;
}
