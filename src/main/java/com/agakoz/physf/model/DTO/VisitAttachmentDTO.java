package com.agakoz.physf.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitAttachmentDTO {

    private int id;
    private String description;
    private int fileId;
    private String fileName;

    public VisitAttachmentDTO(int id, String description, int fileId, String fileName, String fileType) {
        this.id = id;
        this.description = description;
        this.fileId = fileId;
        this.fileName = fileName + "." + fileType;
    }
}
