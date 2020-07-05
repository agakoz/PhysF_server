package com.agakoz.physf.services;

import com.agakoz.physf.model.DTO.PatientCreateOrUpdateDTO;
import com.agakoz.physf.model.DTO.PatientDTO;
import com.agakoz.physf.model.Patient;
import com.agakoz.physf.model.User;
import com.agakoz.physf.repositories.PatientRepository;
import com.agakoz.physf.repositories.UserRepository;
import com.agakoz.physf.security.SecurityUtils;
import com.agakoz.physf.services.exceptions.*;
import com.agakoz.physf.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    PatientRepository patientRepository;
    UserRepository userRepository;

    @Autowired
    public PatientService(PatientRepository patientRepository, UserRepository userRepository) {

        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }
//TODO patient validation
    public void addPatient(PatientCreateOrUpdateDTO patientDTO) throws IllegalArgumentException {

        checkPeselOrThrow(patientDTO.getPesel());
        Patient newPatient = ObjectMapperUtils.map(patientDTO, new Patient());
        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new CurrentUserException("Current user login not found"));
        User currentUser = userRepository.findByUsername(currentUsername).get();
        newPatient.setUser(currentUser);
        patientRepository.save(newPatient);

    }

    public void updatePatient(int patientId, PatientCreateOrUpdateDTO patientDTO) throws IllegalArgumentException {
        patientExistsOrThrow(patientId);
        Patient oldPatient = getPatient(patientId);
        Patient updatedPatient = ObjectMapperUtils.map(patientDTO, oldPatient);

        checkPeselForExistingUserOrThrow(patientId, updatedPatient.getPesel());
        patientRepository.save(updatedPatient);

    }

    public void deletePatient(int id) throws PatientWithIdNotExistsException, CurrentUserException {
        validatePatientIdForCurrentUser(id);
        Patient patientToDelete = getPatient(id);
        patientRepository.delete(patientToDelete);

    }

    public void deleteAllPatientsFromCurrentUser() throws NoPatientsException, CurrentUserException {
        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new CurrentUserException("Current user login not found"));
        List<Integer> patientIds = patientRepository.getIdsByUserId(currentUsername);
        if (patientIds.isEmpty()) {
            throw new NoPatientsException();
        }
        patientRepository.deleteAllFromUser(currentUsername);
    }

    public List<PatientDTO> getAllPatientsFromCurrentUser() throws NoPatientsException, CurrentUserException {
        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new CurrentUserException("Current user login not found"));
        List<PatientDTO> patients = patientRepository.retrievePatientsDTOByUserId(currentUsername);
        if (patients.isEmpty()) {
            throw new NoPatientsException();
        } else {
            return patients;
        }
    }

    public PatientDTO getPatientByIdFromCurrentUser(int patientId) throws PatientWithIdNotExistsException, CurrentUserException {
        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new CurrentUserException("Current user login not found"));
        Optional<PatientDTO> patient = patientRepository.retrievePatientDTOByUsernameAndPatientId(currentUsername, patientId);
        if (patient.isPresent()) {
            return patient.get();
        } else {
            throw new PatientWithIdNotExistsException(patientId);
        }
    }

    private boolean patientExists(int id) {
        Optional<Patient> patient = patientRepository.findById(id);
        return patient.isPresent();
    }

    private void patientExistsOrThrow(int id) throws PatientWithIdNotExistsException {
        if (!patientExists(id))
            throw new PatientWithIdNotExistsException(id);
    }

    public Patient getPatient(int id) throws PatientWithIdNotExistsException {
        patientExistsOrThrow(id);
        return patientRepository.findById(id).get();

    }

//    private User getCurrentUser() throws CurrentUserException {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        User currentUser;
//        if (principal instanceof UserDetails) {
//            currentUser = ((User) (principal));
//
//        } else {
//            throw new CurrentUserException("");
//
//        }
//        return currentUser;
//    }

    private void checkPeselOrThrow(String pesel) throws PeselIsNullException, BadLengthPeselException,PersonWithPeselAlreadyExistsException, CurrentUserException {
        if (pesel == null) throw new PeselIsNullException();
        if (!isPeselUnique(pesel)) throw new PersonWithPeselAlreadyExistsException("User");
        if (!isPeselOfGoodLength(pesel)) throw new BadLengthPeselException();
    }

    private void checkPeselForExistingUserOrThrow(int patientId, String pesel) throws IllegalArgumentException, CurrentUserException {
        if (pesel == null) throw new PeselIsNullException();
        if (!isPeselUnique(pesel, patientId)) throw new PersonWithPeselAlreadyExistsException("User");
        if (!isPeselOfGoodLength(pesel)) throw new BadLengthPeselException();
    }

    private boolean isPeselUnique(String pesel) throws CurrentUserException {

        if (pesel.length() > 0) {
            String currentUsername = SecurityUtils
                    .getCurrentUserUsername()
                    .orElseThrow(() -> new CurrentUserException("Current user login not found"));
            List<Integer> patientsWithTheSamePesel = patientRepository.findByPesel(pesel, currentUsername);
            return patientsWithTheSamePesel.size() == 0;
        }
        return false;
    }

    private boolean isPeselUnique(String pesel, int patientId) throws CurrentUserException {

        if (pesel.length() > 0) {
            String currentUsername = SecurityUtils
                    .getCurrentUserUsername()
                    .orElseThrow(() -> new CurrentUserException("Current user login not found"));
            List<Integer> patientsWithTheSamePesel = patientRepository.findByPesel(pesel, currentUsername);

            return patientsWithTheSamePesel.size() == 1 && patientsWithTheSamePesel.contains(patientId);
        }
        return false;
    }

    private boolean isPeselOfGoodLength(String pesel) {

        return pesel.length() == 11;
    }

    public void validatePatientIdForCurrentUser(int patientId) throws PatientWithIdNotExistsException, CurrentUserException {
        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new CurrentUserException("Current user login not found"));
        List<Integer> patients = patientRepository.getByIdAndCurrent(patientId, currentUsername);
        if (patients.size() == 0)
            throw new PatientWithIdNotExistsException(patientId);
    }


}
