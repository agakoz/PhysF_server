package com.agakoz.physf.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreatmentCycleAttachmentsWrapper {

    private List<TreatmentCycleAttachmentDTO> attachments;
}
