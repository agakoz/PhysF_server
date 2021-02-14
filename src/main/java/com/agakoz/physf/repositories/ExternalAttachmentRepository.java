package com.agakoz.physf.repositories;

import com.agakoz.physf.model.ExternalAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ExternalAttachmentRepository extends JpaRepository<ExternalAttachment, Integer> {
    @Query("SELECT max(id) from ExternalAttachment ")
    Optional<Integer> getLastId();
}
