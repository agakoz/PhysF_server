package com.agakoz.physf.repositories;

import com.agakoz.physf.model.DTO.FinishedVisitDTO;
import com.agakoz.physf.model.DTO.VisitCalendarEvent;
import com.agakoz.physf.model.DTO.VisitDateTimeInfo;
import com.agakoz.physf.model.DTO.VisitPlanDTO;
import com.agakoz.physf.model.Visit;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface VisitRepository extends JpaRepository<Visit, Integer> {

    @Query("SELECT new com.agakoz.physf.model.DTO.VisitDateTimeInfo(v.id, v.date, v.startTime, v.endTime) from Visit v " +
            "where v.treatmentCycle.id = :treatmentCycleId " +
            "AND v.finished = true")
    List<VisitDateTimeInfo> findFinishedVisitDateTimeInfoByTreatmentCycleId(int treatmentCycleId);

    @Query("SELECT new com.agakoz.physf.model.DTO.VisitPlanDTO(v.id, v.date, v.startTime, v.endTime, v.treatmentCycle.id, v.treatmentCycle.title, c.patient.id, v.notes) " +
            "FROM Visit v " +
            "JOIN TreatmentCycle c ON v.treatmentCycle.id = c.id " +
            "WHERE v.date >= :now " +
            "AND c.patient.id = :patientId " +
            "AND c.user.username = :username " +
            "AND v.finished = false")
    List<VisitPlanDTO> findIncomingVisit(int patientId, LocalDate now, String username);


    @Query("SELECT new com.agakoz.physf.model.DTO.VisitCalendarEvent(" +
            "v.id, v.date, v.startTime, v.endTime, c.title, c.patient.id, c.patient.birthDate, c.patient.name, c.patient.surname, v.finished) " +
            "FROM Visit v " +
            "JOIN TreatmentCycle c ON v.treatmentCycle.id = c.id " +
            "WHERE c.user.username = :username")
    List<VisitCalendarEvent> retrieveAllVisitsAsCalendarEvent(String username);

    @Query("SELECT new com.agakoz.physf.model.DTO.VisitPlanDTO(v.id, v.date, v.startTime, v.endTime, v.treatmentCycle.id, v.treatmentCycle.title, c.patient.id, v.notes) " +
            "FROM Visit v " +
            "JOIN TreatmentCycle c " +
            "ON v.treatmentCycle.id = c.id " +
            "WHERE v.id= :visitId " +
            "AND c.user.username = :currentUsername")
    Optional<VisitPlanDTO> findIncomingVisit(int visitId, String currentUsername);

    @Query("SELECT new com.agakoz.physf.model.DTO.FinishedVisitDTO(" +
            "v.id, " +
            "v.date, " +
            "v.startTime, " +
            "v.endTime, " +
            "v.treatmentCycle.id, " +
            "v.treatmentCycle.title, " +
            "c.patient.id, " +
            "v.notes," +
            "v.treatment) " +
            "FROM Visit v " +
            "JOIN TreatmentCycle c " +
            "ON v.treatmentCycle.id = c.id " +
            "WHERE v.id= :visitId " +
            "AND c.user.username = :currentUsername " +
            "AND v.finished = true")
    Optional<FinishedVisitDTO> retrieveVisitAsFinishedVisitDTO(int visitId, String currentUsername);

    @Query("SELECT CASE WHEN count(v)>0 then true else false end " +
                    "from Visit v " +
                    "where v.id <> :visitId " +
                    "AND v.date = :date " +
                    "and ( :startTime Between v.startTime and v.endTime " +
                    "OR :endTime Between v.startTime and v.endTime " +
                    "OR :startTime <= v.startTime and :endTime >= v.endTime) " +
                    "AND v.treatmentCycle.user.username = :username")
    boolean isVisitPlannedForGivenDateAndTime(String username, int visitId, LocalDate date, LocalTime startTime, LocalTime endTime);

    @Query("SELECT new com.agakoz.physf.model.DTO.FinishedVisitDTO(" +
            "v.id, " +
            "v.date, " +
            "v.startTime, " +
            "v.endTime, " +
            "v.treatmentCycle.id, " +
            "v.treatmentCycle.title, " +
            "c.patient.id, " +
            "v.notes," +
            "v.treatment) " +
            "FROM Visit v " +
            "JOIN TreatmentCycle c " +
            "ON v.treatmentCycle.id = c.id " +
            "WHERE c.id = :treatmentCycleId " +
            "AND c.user.username = :currentUsername " +
            "AND v.finished = true")
    List<FinishedVisitDTO> retrieveAllFinishedVisitsAsDTOByTreatmentCycleId(String currentUsername, int treatmentCycleId);

    @Query("SELECT new com.agakoz.physf.model.DTO.VisitCalendarEvent(" +
            "v.id, v.date, v.startTime, v.endTime, c.title, c.patient.id, c.patient.birthDate, c.patient.name, c.patient.surname, v.finished) " +
            "FROM Visit v " +
            "JOIN TreatmentCycle c ON v.treatmentCycle.id = c.id " +
            "WHERE v.id = :visitId")
    Optional<VisitCalendarEvent> retrieveVisitAsCalendarEvent(int visitId);


    @Query("select max(v.id) from Visit v where v.treatmentCycle.user.username = :currentUsername")
    int getLastVisitId(String currentUsername);

}
