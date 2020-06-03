package com.agakoz.physf.services;

import com.agakoz.physf.model.DTO.PatientCreateOrUpdateDTO;
import com.agakoz.physf.model.DTO.PatientDTO;
import com.agakoz.physf.model.Patient;
import com.agakoz.physf.model.User;
import com.agakoz.physf.repositories.PatientRepository;
import com.agakoz.physf.utils.ObjectMapperUtils;
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

    public void addPatient(PatientCreateOrUpdateDTO patientDTO) throws IOException {

        checkPeselOrThrow(patientDTO.getPesel());
        Patient newPatient = ObjectMapperUtils.map(patientDTO, new Patient() );
        newPatient.setUser(getCurrentUser());
        patientRepository.save(newPatient);

    }

    public void updatePatient(int patientId, PatientCreateOrUpdateDTO patientDTO) throws IOException {
        patientExistsOrThrow(patientId);
        Patient oldPatient = getPatient(patientId);
        Patient updatedPatient = ObjectMapperUtils.map(patientDTO, oldPatient);

        checkPeselForExistingUserOrThrow(patientId, updatedPatient.getPesel());
        patientRepository.save(updatedPatient);

    }

    public void deletePatient(int id) throws IOException {
        checkPatientOfCurrentUserOrThrow(id);
        Patient patientToDelete = getPatient(id);
        patientRepository.delete(patientToDelete);

    }

    public void deleteAllPatientsFromCurrentUser() throws IOException {
        ;
        List<Integer> patientIds = patientRepository.getIdsByUserId(getCurrentUserId());
        if (patientIds.isEmpty()) {
            throw new IOException("User has no patients to delete.");
        }
        patientRepository.deleteAllFromUser(getCurrentUserId());
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
    private User getCurrentUser() throws IOException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User currentUser;
        if (principal instanceof UserDetails) {
            currentUser = ((User) (principal));

        } else {
            currentUser = null;

        }
        return currentUser;
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

    private void checkPeselForExistingUserOrThrow(int patientId, String pesel) throws IOException {
        if (pesel == null) throw new IllegalArgumentException("Pesel is obligatory");
        if (!isPeselUnique(pesel, patientId)) throw new IllegalArgumentException("Patient with this pesel already exists");
        if (!isPeselOfGoodLength(pesel)) throw new IllegalArgumentException("pesel should consist of 11 digits");
    }

    private boolean isPeselUnique(String pesel) throws IOException {

        if (pesel.length() > 0) {
            int currentUserId = getCurrentUserId();
            List<Integer> patientsWithTheSamePesel = patientRepository.findByPesel(pesel, currentUserId);
            return patientsWithTheSamePesel.size() == 0;
        }
        return false;
    }

    private boolean isPeselUnique(String pesel, int patientId) throws IOException {

        if (pesel.length() > 0) {
            int currentUserId = getCurrentUserId();
            List<Integer> patientsWithTheSamePesel = patientRepository.findByPesel(pesel, currentUserId);

            return patientsWithTheSamePesel.size() == 1 && patientsWithTheSamePesel.contains(patientId);
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

//    private Patient updateEntity(int patientId, PatientCreateOrUpdateDTO patientDTO) throws IOException {
//        Patient patient = getPatient(patientId);
//        patient.setName(patientDTO.getName());
//        patient.setSurname(patientDTO.getSurname());
//        patient.setBirthDate(patientDTO.getBirthDate());
//        patient.setPesel(patientDTO.getPesel());
//        patient.setSex(patientDTO.getSex());
//        patient.setAddress(patientDTO.getAddress());
//        patient.set
//
//
//    }
}
