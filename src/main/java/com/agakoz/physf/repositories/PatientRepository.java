package com.agakoz.physf.repositories;

import com.agakoz.physf.model.DTO.PatientDTO;
import com.agakoz.physf.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    @Query("SELECT new com.agakoz.physf.model.DTO.PatientDTO" +
            "(p.id, p.user.id, p.name, p.surname, p.birthDate, p.pesel, p.sex, p.address, p.city, p.email, p.phone, p.lifestyle, " +
            "p.profession, p.guardian, p.pastDiseases, p.chronicDiseases, p.hospitalization, p.surgeries, p.pastTreatment, " +
            "p.allergies, p.familyDiseases, p.medicalCertificate, p.extraDetails) " +
            "FROM Patient p " +
            "where p.user.id = :userId")
    List<PatientDTO> retrievePatientsDTOByUserId (@Param("userId") int userId);


    @Query("SELECT new com.agakoz.physf.model.DTO.PatientDTO" +
            "(p.id, p.user.id, p.name, p.surname, p.birthDate, p.pesel, p.sex, p.address, p.city, p.email, p.phone, p.lifestyle, " +
            "p.profession, p.guardian, p.pastDiseases, p.chronicDiseases, p.hospitalization, p.surgeries, p.pastTreatment, " +
            "p.allergies, p.familyDiseases, p.medicalCertificate, p.extraDetails) FROM Patient p where p.id = :id and p.user.id= :userId")
    Optional<PatientDTO> retrievePatientDTOByUserIdAndPatientId(@Param("userId")int userId, @Param("id") int patientId);

    @Query("SELECT p.id FROM Patient p where p.pesel = :pesel and p.user.id = :userId  ")
    List<String> findByPesel( @Param("pesel") String pesel, @Param("userId") int userId);

    @Query("SELECT p.id FROM Patient p where p.id = :patientId and p.user.id = :userId  ")
    List<Integer> getByIdAndCurrent( @Param("patientId") int patientId, @Param("userId") int userId);

}
