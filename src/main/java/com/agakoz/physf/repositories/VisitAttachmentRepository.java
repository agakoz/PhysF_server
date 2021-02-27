package com.agakoz.physf.repositories;

import com.agakoz.physf.model.DTO.ExternalAttachmentDTO;
import com.agakoz.physf.model.DTO.VisitAttachmentDTO;
import com.agakoz.physf.model.ExternalAttachment;
import com.agakoz.physf.model.VisitAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VisitAttachmentRepository extends JpaRepository<VisitAttachment, Integer> {
    @Query("SELECT max(id) from VisitAttachment ")
    Optional<Integer> getLastId();

    @Query("SELECT a.id from VisitAttachment a where a.visit.id = :visitId")
    List<Integer> findAllIdByVisitId(int visitId);

    @Query("SELECT new com.agakoz.physf.model.DTO.VisitAttachmentDTO(a.id, a.description, a.file.id, a.file.name, a.file.type) " +
            "from VisitAttachment a " +
            "where a.visit.id = :visitId")
    List<VisitAttachmentDTO> findAttachmentsAssignedToVisit(int visitId);
}
