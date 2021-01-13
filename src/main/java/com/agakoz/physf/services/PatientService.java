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

import javax.transaction.Transactional;
import java.util.ArrayList;
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
        System.out.println("adding patient");
        System.out.println(patientDTO);
        checkPeselOrThrow(patientDTO.getPesel());
        Patient newPatient = ObjectMapperUtils.map(patientDTO, new Patient());
        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new UserException("Current user login not found"));
        User currentUser = userRepository.findByUsername(currentUsername).get();
        newPatient.setUser(currentUser);

        patientRepository.save(newPatient);

    }

    public PatientCreateOrUpdateDTO updatePatient(int patientId, PatientCreateOrUpdateDTO patientDTO) throws IllegalArgumentException {
        patientExistsOrThrow(patientId);
        Patient oldPatient = getPatient(patientId);
        Patient updatedPatient = ObjectMapperUtils.map(patientDTO, oldPatient);

        checkPeselForExistingUserOrThrow(patientId, updatedPatient.getPesel());
        patientRepository.save(updatedPatient);
        return patientDTO;

    }

    public void deletePatients(List<Integer> ids) throws PatientWithIdNotExistsException, UserException {
        for (int id : ids) {
            deletePatient(id);
        }
    }

    public void deletePatient(int id) throws PatientWithIdNotExistsException, UserException {
        validatePatientIdForCurrentUser(id);
        Patient patientToDelete = getPatient(id);
        patientRepository.delete(patientToDelete);
    }

    @Transactional
    public void deleteAllPatientsFromCurrentUser() throws NoPatientsException, UserException {
        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new UserException("Current user login not found"));
        Optional<List<Patient>> patientList = patientRepository.getPatientsFromUser(currentUsername);
        System.out.println(patientList);
        if (!patientList.isPresent()) {
            throw new NoPatientsException();
        }
        for (Patient p : patientList.get()) {
            patientRepository.delete(p);
        }
    }

    public List<PatientDTO> getAllPatientsFromCurrentUser() throws NoPatientsException, UserException {
        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new UserException("Current user login not found"));
        List<PatientDTO> patients = patientRepository.retrievePatientsDTOByUserId(currentUsername);
//        if (patients.isEmpty()) {
//            throw new NoPatientsException();
//        } else {
        return patients;
//        }
    }

    public PatientDTO getPatientByIdFromCurrentUser(int patientId) throws PatientWithIdNotExistsException, UserException {
        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new UserException("Current user login not found"));
        Optional<PatientDTO> patient = patientRepository.retrievePatientDTOByUsernameAndPatientId(currentUsername, patientId);
        if (patient.isPresent()) {
            return patient.get();
        } else {
            throw new PatientWithIdNotExistsException(patientId);
        }
    }

    public boolean patientExists(int id) {
        Optional<Patient> patient = patientRepository.findById(id);
        return patient.isPresent();
    }

    public void patientExistsOrThrow(int id) throws PatientWithIdNotExistsException {
        if (!patientExists(id))
            throw new PatientWithIdNotExistsException(id);
    }

    public Patient getPatient(int id) throws PatientWithIdNotExistsException {
        patientExistsOrThrow(id);
        return patientRepository.findById(id).get();

    }


    private void checkPeselOrThrow(String pesel) throws PeselIsNullException, BadLengthPeselException, PersonWithPeselAlreadyExistsException, UserException {
        if (pesel == null) throw new PeselIsNullException();
        if (!isPeselUnique(pesel)) throw new PersonWithPeselAlreadyExistsException("Patient");
        if (!isPeselOfGoodLength(pesel)) throw new BadLengthPeselException();
    }

    private void checkPeselForExistingUserOrThrow(int patientId, String pesel) throws IllegalArgumentException, UserException {
        if (pesel == null) throw new PeselIsNullException();
        if (!isPeselOfGoodLength(pesel)) throw new BadLengthPeselException();
        if (!isPeselUnique(pesel, patientId)) throw new PersonWithPeselAlreadyExistsException("User");

    }

    private boolean isPeselUnique(String pesel) throws UserException {

        if (pesel.length() > 0) {
            String currentUsername = SecurityUtils
                    .getCurrentUserUsername()
                    .orElseThrow(() -> new UserException("Current user login not found"));
            List<Integer> patientsWithTheSamePesel = patientRepository.findByPesel(pesel, currentUsername);
            return patientsWithTheSamePesel.size() == 0;
        }
        return false;
    }

    private boolean isPeselUnique(String pesel, int patientId) throws UserException {

        if (pesel.length() > 0) {
            String currentUsername = SecurityUtils
                    .getCurrentUserUsername()
                    .orElseThrow(() -> new UserException("Current user login not found"));
            List<Integer> patientsWithTheSamePesel = patientRepository.findByPesel(pesel, currentUsername);

            return (patientsWithTheSamePesel.size() == 1 && patientsWithTheSamePesel.contains(patientId)) || patientsWithTheSamePesel.size() == 0;

        }
        return false;
    }

    private boolean isPeselOfGoodLength(String pesel) {

        return pesel.length() == 11;
    }

    public void validatePatientIdForCurrentUser(int patientId) throws PatientWithIdNotExistsException, UserException {
        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new UserException("Current user login not found"));
        List<Integer> patients = patientRepository.getByIdAndCurrent(patientId, currentUsername);
        if (patients.size() == 0)
            throw new PatientWithIdNotExistsException(patientId);
    }
}
