package com.agakoz.physf.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Table(name = "external_attachments")
public class ExternalAttachment {
    @Id
    @Column
    private int id;

    @ManyToOne
    @JoinColumn(name = "treatment_cycle_id", referencedColumnName = "id", nullable = false)
    @NonNull
    private TreatmentCycle treatmentCycle;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name = "uploaded_file_id", referencedColumnName = "id", nullable = true)
    private UploadedFile file;

    @Column
    private String description;

    @Column
    private  String link;

}
