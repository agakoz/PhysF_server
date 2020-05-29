package com.agakoz.physf.model;

import lombok.*;

import javax.persistence.*;

@Table(name = "photos")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JoinColumn(name = "visit_id", referencedColumnName = "id", nullable = false)
    @NonNull
    private Visit visit;

    @Column(columnDefinition = "varbinary(max)")
    @Lob
    @NonNull
    private byte[] photo;

    @Column
    private String description;

}
