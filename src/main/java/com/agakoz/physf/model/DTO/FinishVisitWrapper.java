package com.agakoz.physf.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinishVisitWrapper {

    private List<TreatmentCycleAttachmentDTO> attachments;
    private VisitToFinishDTO visit;
    private TreatmentCycleInfoDTO treatmentCycle;
}
