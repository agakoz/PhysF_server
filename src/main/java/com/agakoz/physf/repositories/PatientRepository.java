package com.agakoz.physf.repositories;

import com.agakoz.physf.model.DTO.PatientDTO;
import com.agakoz.physf.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    @Query("SELECT new com.agakoz.physf.model.DTO.PatientDTO" +
            "(p.id, p.name, p.surname, p.birthDate, p.pesel, p.sex, p.address, p.city, p.email, p.phone, p.lifestyle, " +
            "p.profession, p.guardian, p.pastDiseases, p.chronicDiseases, p.hospitalization, p.surgeries, p.pastTreatment, " +
            "p.allergies, p.familyDiseases, p.medicalCertificate, p.extraDetails) " +
            "FROM Patient p " +
            "where p.user.username = :username")
    List<PatientDTO> retrievePatientsDTOByUserId(@Param("username") String currentUsername);


    @Query("SELECT new com.agakoz.physf.model.DTO.PatientDTO" +
            "(p.id, p.name, p.surname, p.birthDate, p.pesel, p.sex, p.address, p.city, p.email, p.phone, p.lifestyle, " +
            "p.profession, p.guardian, p.pastDiseases, p.chronicDiseases, p.hospitalization, p.surgeries, p.pastTreatment, " +
            "p.allergies, p.familyDiseases, p.medicalCertificate, p.extraDetails) FROM Patient p where p.id = :id and p.user.username= :username")
    Optional<PatientDTO> retrievePatientDTOByUsernameAndPatientId(@Param("username") String currentUsername, @Param("id") int patientId);

    @Query("SELECT p.id FROM Patient p where p.pesel = :pesel and p.user.username = :username  ")
    List<Integer> findByPesel(@Param("pesel") String pesel, @Param("username") String currentUsername);

    @Query("SELECT p.id FROM Patient p where p.id = :patientId and p.user.username = :username  ")
    List<Integer> getByIdAndCurrent(@Param("patientId") int patientId, @Param("username") String currentUsername);

    @Query("SELECT p FROM Patient p where  p.user.username = :username  ")
    Optional<List<Patient>> getPatientsFromUser(@Param("username") String currentUsername);
}
