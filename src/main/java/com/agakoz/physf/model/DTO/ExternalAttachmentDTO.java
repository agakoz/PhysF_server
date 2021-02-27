package com.agakoz.physf.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExternalAttachmentDTO {

    private int id;
    private String description;
    private String link;
    private int fileId;
    private String fileName;

    public ExternalAttachmentDTO(int id, String description, String link, int fileId) {
        this.id = id;
        this.description = description;
        this.link = link;
        this.fileId = fileId;
    }
}
