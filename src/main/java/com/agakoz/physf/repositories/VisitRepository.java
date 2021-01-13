package com.agakoz.physf.repositories;

import com.agakoz.physf.model.DTO.FirstVisitPlanDTO;
import com.agakoz.physf.model.DTO.IncomingVisitDTO;
import com.agakoz.physf.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface VisitRepository extends JpaRepository<Visit, Integer> {
    @Query("SELECT new com.agakoz.physf.model.DTO.IncomingVisitDTO(v.id, v.date, v.startTime, v.endTime, v.treatmentCycle.id, v.treatmentCycle.title, v.notes) " +
            "FROM Visit v " +
            "JOIN TreatmentCycle c ON v.treatmentCycle.id = c.id " +
            "WHERE v.date >= :now AND c.patient.id = :patientId")
    List<IncomingVisitDTO> findIncomingVisit(int patientId, LocalDate now);

//        @Query("SELECT new com.agakoz.physf.model.DTO.VisitDTO(v.id, v.patient.id, v.date, v.startTime, v.endTime, v.title, v.symptoms, v.firstTime, v.diagnosis, v.recommendations," +
//            "v.examinationDesc, v.treatment, v.notes )" +
//            " FROM Visit v LEFT OUTER JOIN Photo p on v.id = p.visit.id WHERE v.user.id= :userId")
//    List<Optional<IncomingVisitDTO>> getIncomingVisitsForPatient(@Param("id") int patientId);


//    @Query("SELECT new com.agakoz.physf.model.DTO.VisitDTO(v.id, v.patient.id, v.date, v.startTime, v.endTime, v.title, v.symptoms, v.firstTime, v.diagnosis, v.recommendations," +
//            "v.examinationDesc, v.treatment, v.notes )" +
//            " FROM Visit v LEFT OUTER JOIN Photo p on v.id = p.visit.id WHERE v.user.id= :userId")
//    List<VisitDTO> retrieveVisitDTOsByUserId(@Param("userId") int id);
//
//    @Query("SELECT new com.agakoz.physf.model.DTO.VisitDTO(v.id, v.patient.id, v.date, v.startTime, v.endTime, v.title, v.symptoms, v.firstTime, v.diagnosis, v.recommendations," +
//            "v.examinationDesc, v.treatment, v.notes )" +
//            " FROM Visit v WHERE v.user.id= :userId and v.patient.id= :patientId")
//    List<VisitDTO> retrieveVisitDTOsByUserIdPatientId(@Param("userId") int userId, @Param("patientId") int patientId);
//
//    @Query("SELECT new com.agakoz.physf.model.DTO.VisitDTO(v.id, v.patient.id, v.date, v.startTime, v.endTime, v.title, v.symptoms, v.firstTime, v.diagnosis, v.recommendations," +
//            "v.examinationDesc, v.treatment, v.notes )" +
//            " FROM Visit v WHERE v.id= :visitId")
//    Optional<VisitDTO> retrieveVisitDTOById(@Param("visitId") int id);
//
//        @Query("SELECT new com.agakoz.physf.model.DTO.VisitPlanDTO(v.id, v.patient.id, v.date, v.startTime, v.endTime, v.title) " +
//            "FROM Visit v " +
//            "WHERE v.user.id = :userId")
//    List<VisitPlanDTO> retrieveVisitPlanDTOsByUserId(@Param("userId") int userId);
//
//    @Query("SELECT new com.agakoz.physf.model.DTO.VisitPlanDTO(v.id, v.patient.id, v.date, v.startTime, v.endTime, v.title) " +
//            "FROM Visit v " +
//            "WHERE v.id = :visitId")
//    Optional<VisitPlanDTO> retrieveVisitPlanDTOById(@Param("visitId") int visitId);
//
//    @Query("SELECT new com.agakoz.physf.model.DTO.VisitPlanDTO(v.id, v.patient.id, v.date, v.startTime, v.endTime, v.title) " +
//            "FROM Visit v " +
//            "WHERE v.user.id = :userId AND v.date= :date")
//    List<VisitPlanDTO> retrieveVisitPlanDTOsByDateUserId(@Param("date") Date date,@Param("userId") int id);

//    @Query("SELECT v FROM PlannedVisit v WHERE v.id = :visitId")
//    Optional<Visit> getById(@Param("visitId") int visitId);
}
