package com.agakoz.physf.model.DTO;

import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreatmentCycleAttachmentDTO {

    private int id;
    private String description;
    private String fileName;
    private String link;
    private int fileId;
//    private int treatmentCycleId;
//    private
}
