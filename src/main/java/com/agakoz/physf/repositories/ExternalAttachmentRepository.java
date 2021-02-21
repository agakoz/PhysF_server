package com.agakoz.physf.repositories;

import com.agakoz.physf.model.DTO.TreatmentCycleAttachmentDTO;
import com.agakoz.physf.model.ExternalAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ExternalAttachmentRepository extends JpaRepository<ExternalAttachment, Integer> {
    @Query("SELECT max(id) from ExternalAttachment ")
    Optional<Integer> getLastId();

    @Query("SELECT new com.agakoz.physf.model.DTO.TreatmentCycleAttachmentDTO(a.id, a.description, a.link, coalesce(a.file.id, -1)) from ExternalAttachment a where a.treatmentCycle.id = :treatmentCycleId")
    List<TreatmentCycleAttachmentDTO> findAttachmentsAssignedToTreatmentCycle(int treatmentCycleId);

    @Query("SELECT a.id from ExternalAttachment a where a.treatmentCycle.id = :treatmentCycleId")
    List<Integer> findAllIdByTreatmentCycleId(int treatmentCycleId);

}
