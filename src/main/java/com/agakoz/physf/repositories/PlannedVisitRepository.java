package com.agakoz.physf.repositories;

import com.agakoz.physf.model.DTO.PlannedVisitDTO;
import com.agakoz.physf.model.PlannedVisit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlannedVisitRepository extends JpaRepository<PlannedVisit, Integer> {
    @Query("SELECT new com.agakoz.physf.model.DTO.PlannedVisitDTO(iv.id, iv.patient.id, iv.date, iv.startTime, iv.endTime, iv.note) " +
            "FROM PlannedVisit iv " +
            "WHERE iv.user.id = :userId")
    List<PlannedVisitDTO> retrieveAllAsDTOByUserId(@Param("userId") int userId);

    @Query("SELECT new com.agakoz.physf.model.DTO.PlannedVisitDTO(iv.id, iv.patient.id, iv.date, iv.startTime, iv.endTime, iv.note) " +
            "FROM PlannedVisit iv " +
            "WHERE iv.id = :visitId")
    Optional<PlannedVisitDTO> retrieveAsDTOById(@Param("visitId") int visitId);

    @Query("SELECT v FROM PlannedVisit v WHERE v.id = :visitId")
    Optional<PlannedVisit> getById(@Param("visitId") int visitId);
}
