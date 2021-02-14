package com.agakoz.physf.model;

import lombok.*;

import javax.persistence.*;

@Table(name = "uploaded_files")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadedFile {
    @Id
    @Column
    private int id;

    @Column
    private String name;

    @Column
    private String type;

    @Column(columnDefinition = "varbinary(max)")
    @Lob
    @NonNull
    private byte[] data;

}
