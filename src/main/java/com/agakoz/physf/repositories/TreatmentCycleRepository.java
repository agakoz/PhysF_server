package com.agakoz.physf.repositories;

import com.agakoz.physf.model.DTO.TreatmentCycleTitleDTO;
import com.agakoz.physf.model.TreatmentCycle;
import com.agakoz.physf.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TreatmentCycleRepository extends JpaRepository<TreatmentCycle, Integer> {
    @Query("SELECT new com.agakoz.physf.model.DTO.TreatmentCycleTitleDTO(c.id, c.title) " +
            "FROM TreatmentCycle c WHERE c.patient.id = :patientId")
    List<TreatmentCycleTitleDTO> findAllByPatientId(int patientId);

    @Query(nativeQuery = true, value = "SELECT TOP 1 v.id from visits v join treatment_cycles on v.treatment_cycle_id = :treatmentCycleId ")
    List<Integer> findFirstVisitConnectedToCycle(int treatmentCycleId);
}
