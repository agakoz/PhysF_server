package com.agakoz.physf.services;

import com.agakoz.physf.model.DTO.PatientDTO;
import com.agakoz.physf.model.Patient;
import com.agakoz.physf.model.User;
import com.agakoz.physf.repositories.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    PatientRepository patientRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository) {

        this.patientRepository = patientRepository;
    }

    public void addPatient(Patient patient) throws IOException {

        checkPeselOrThrow(patient.getPesel());
        patientRepository.save(patient);
    }

    public void updatePatient(int id, Patient patient) throws IOException {
        patientExistsOrThrow(id);
        if (id == patient.getId()) {
            checkPeselOrThrow(patient.getPesel());
            patientRepository.save(patient);
        } else
            throw new IOException("id of patient to be updated is wrong!");
    }

    public void deletePatient(int id) throws IOException {
        checkPatientOfCurrentUserOrThrow(id);
        Patient patientToDelete = getPatient(id);
        patientRepository.delete(patientToDelete);

    }

    public List<PatientDTO> getAllPatientsFromCurrentUser() throws IOException {
        int userId = getCurrentUserId();
        List<PatientDTO> patients = patientRepository.retrievePatientsDTOByUserId(userId);
        if (patients.isEmpty()) {
            throw new IOException("User has no patients.");
        } else {
            return patients;
        }
    }

    public PatientDTO getPatientByIdFromCurrentUser(int patientId) throws IOException {
        int currentId = getCurrentUserId();
        Optional<PatientDTO> patient = patientRepository.retrievePatientDTOByUserIdAndPatientId(currentId, patientId);
        if (patient.isPresent()) {
            return patient.get();
        } else {
            throw new IOException(String.format("No user with id: \"%d\" assigned to the current user", patientId));
        }
    }

    private boolean patientExists(int id) {
        Optional<Patient> patient = patientRepository.findById(id);
        return patient.isPresent();
    }

    private void patientExistsOrThrow(int id) {
        if (!patientExists(id))
            throw new IllegalArgumentException(String.format("patient with id: \"%d\" does not exist ", id));
    }

    private Patient getPatient(int id) throws IOException {
        patientExistsOrThrow(id);
        return patientRepository.findById(id).get();

    }

    private int getCurrentUserId() throws IOException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int currentUserId;
        if (principal instanceof UserDetails) {
            currentUserId = ((User) (principal)).getId();

        } else {
            currentUserId = -1;

        }
        return currentUserId;
    }


    private void checkPeselOrThrow(String pesel) throws IOException {
        if (pesel == null) throw new IllegalArgumentException("Pesel is obligatory");
        if (!isPeselUnique(pesel)) throw new IllegalArgumentException("Patient with this pesel already exists");
        if (!isPeselOfGoodLength(pesel)) throw new IllegalArgumentException("pesel should consist of 11 digits");
    }

    private boolean isPeselUnique(String pesel) throws IOException {

        if (pesel.length() > 0) {
            int currentUserId = getCurrentUserId();
            List<String> patientsWithTheSamePesel = patientRepository.findByPesel(pesel, currentUserId);
            return patientsWithTheSamePesel.size() == 0;
        }
        return false;
    }

    private boolean isPeselOfGoodLength(String pesel) {

        return pesel.length() == 11;
    }

    private void checkPatientOfCurrentUserOrThrow(int patientId) throws IOException {
        int currentUserId = getCurrentUserId();
        List<Integer> patients = patientRepository.getByIdAndCurrent(patientId, currentUserId);
        if (patients.size() == 0)
            throw new IOException(String.format("User has no patient with id= \"%d\"", patientId));
    }
}
