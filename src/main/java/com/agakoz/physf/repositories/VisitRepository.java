package com.agakoz.physf.repositories;

import com.agakoz.physf.model.DTO.VisitDTO;
import com.agakoz.physf.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<Visit, Integer> {
    @Query("SELECT new com.agakoz.physf.model.DTO.VisitDTO(v.id, v.patient.id, v.date, v.reason, v.symptoms, v.diagnosis, v.recommendations," +
            "v.examinationDesc, v.treatment, v.notes )" +
            " FROM Visit v LEFT OUTER JOIN Photo p on v.id = p.visit.id WHERE v.user.id= :userId")
    List<VisitDTO> findAllByUserId(@Param("userId") int id);

    @Query("SELECT new com.agakoz.physf.model.DTO.VisitDTO(v.id, v.patient.id, v.date, v.reason, v.symptoms, v.diagnosis, v.recommendations," +
            "v.examinationDesc, v.treatment, v.notes )" +
            " FROM Visit v WHERE v.user.id= :userId and v.patient.id= :patientId")
    List<VisitDTO> findByUserAndPatientId(@Param("userId") int userId, @Param("patientId") int patientId);

    @Query("SELECT new com.agakoz.physf.model.DTO.VisitDTO(v.id, v.patient.id, v.date, v.reason, v.symptoms, v.diagnosis, v.recommendations," +
            "v.examinationDesc, v.treatment, v.notes )" +
            " FROM Visit v WHERE v.id= :visitId")
    Optional<VisitDTO> retrieveDTOById(@Param("visitId") int id);
}
