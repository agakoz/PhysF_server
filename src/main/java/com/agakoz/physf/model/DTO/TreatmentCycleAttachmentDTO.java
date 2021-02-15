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
    private String link;
    private int fileId;
    private String fileName;

//    private int treatmentCycleId;
//    private

    public TreatmentCycleAttachmentDTO(int id, String description, String link, int fileId, String fileName, String fileType) {
        this.id = id;
        this.description = description;
        this.link = link;
        this.fileId = fileId;
        this.fileName = fileName + "." +fileType ;
    }
}
