package com.agakoz.physf.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "visit_attachments")
public class VisitAttachment {
    @Id
    @Column
    private int id;

    @ManyToOne
    @JoinColumn(name = "visit_id", referencedColumnName = "id", nullable = false)
    @NonNull
    private Visit visit;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "uploaded_file_id", referencedColumnName = "id", nullable = true)
    private UploadedFile file;

    @Column
    private String description;
}
