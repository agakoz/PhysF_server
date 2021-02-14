package com.agakoz.physf.repositories;

import com.agakoz.physf.model.DTO.TreatmentCycleInfoDTO;
import com.agakoz.physf.model.DTO.TreatmentCycleTitleBodyPartDTO;
import com.agakoz.physf.model.DTO.TreatmentCycleTitleDTO;
import com.agakoz.physf.model.DTO.VisitDateTimeInfo;
import com.agakoz.physf.model.TreatmentCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TreatmentCycleRepository extends JpaRepository<TreatmentCycle, Integer> {
    @Query("SELECT new com.agakoz.physf.model.DTO.TreatmentCycleTitleDTO(c.id, c.title) " +
            "FROM TreatmentCycle c " +
            "WHERE c.patient.id = :patientId " +
            "AND c.title IS NOT NULL " +
            "AND c.archival <> true")
    List<TreatmentCycleTitleDTO> findStartedCyclesByPatientId(int patientId);

    @Query(
            nativeQuery = true,
            value = "SELECT TOP 1 v.id from visits v join treatment_cycles on v.treatment_cycle_id = :treatmentCycleId ")
    List<Integer> findFirstVisitConnectedToCycle(int treatmentCycleId);

    @Query("SELECT new com.agakoz.physf.model.DTO.TreatmentCycleInfoDTO(" +
            "c.id, " +
            "c.patient.id, " +
            "c.title, " +
            "c.description, " +
            "c.bodyPart, " +
            "c.injuryDate," +
            "c.symptoms, " +
            "c.examinationDesc, " +
            "c.diagnosis, " +
            "c.treatment, " +
            "c.recommendations, " +
            "c.notes, " +
            "c.similarPastProblems) " +
            "FROM TreatmentCycle c " +
            "WHERE c.id = :id")
    Optional<TreatmentCycleInfoDTO> retrieveById(@Param("id") int treatmentCycleId);

    @Query("SELECT new com.agakoz.physf.model.DTO.TreatmentCycleTitleBodyPartDTO(c.id, c.title, c.bodyPart) " +
            "FROM TreatmentCycle c " +
            "WHERE c.patient.id = :patientId " +
            "AND c.title IS NOT NULL " +
            "AND c.archival <> true")
    List<TreatmentCycleTitleBodyPartDTO> findAllTreatmentCycleTitleAndBodyPartByPatientId(int patientId);
}
