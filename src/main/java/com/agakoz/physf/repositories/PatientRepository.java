package com.agakoz.physf.repositories;

import com.agakoz.physf.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
}
