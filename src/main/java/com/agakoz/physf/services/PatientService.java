package com.agakoz.physf.services;

import com.agakoz.physf.model.DTO.PatientCreateOrUpdateDTO;
import com.agakoz.physf.model.DTO.PatientDTO;
import com.agakoz.physf.model.Patient;
import com.agakoz.physf.model.User;
import com.agakoz.physf.repositories.PatientRepository;
import com.agakoz.physf.services.exceptions.*;
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
//TODO patient validation
    public void addPatient(PatientCreateOrUpdateDTO patientDTO) throws IllegalArgumentException {

        checkPeselOrThrow(patientDTO.getPesel());
        Patient newPatient = ObjectMapperUtils.map(patientDTO, new Patient());
        newPatient.setUser(getCurrentUser());
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
        checkPatientOfCurrentUserOrThrow(id);
        Patient patientToDelete = getPatient(id);
        patientRepository.delete(patientToDelete);

    }

    public void deleteAllPatientsFromCurrentUser() throws NoPatientsException, CurrentUserException {

        List<Integer> patientIds = patientRepository.getIdsByUserId(getCurrentUser().getId());
        if (patientIds.isEmpty()) {
            throw new NoPatientsException();
        }
        patientRepository.deleteAllFromUser(getCurrentUser().getId());
    }

    public List<PatientDTO> getAllPatientsFromCurrentUser() throws NoPatientsException, CurrentUserException {
        int userId = getCurrentUser().getId();
        List<PatientDTO> patients = patientRepository.retrievePatientsDTOByUserId(userId);
        if (patients.isEmpty()) {
            throw new NoPatientsException();
        } else {
            return patients;
        }
    }

    public PatientDTO getPatientByIdFromCurrentUser(int patientId) throws PatientWithIdNotExistsException, CurrentUserException {
        int currentId = getCurrentUser().getId();
        Optional<PatientDTO> patient = patientRepository.retrievePatientDTOByUserIdAndPatientId(currentId, patientId);
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

    private Patient getPatient(int id) throws PatientWithIdNotExistsException {
        patientExistsOrThrow(id);
        return patientRepository.findById(id).get();

    }

    private User getCurrentUser() throws CurrentUserException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User currentUser;
        if (principal instanceof UserDetails) {
            currentUser = ((User) (principal));

        } else {
            throw new CurrentUserException();

        }
        return currentUser;
    }

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
            int currentUserId = getCurrentUser().getId();
            List<Integer> patientsWithTheSamePesel = patientRepository.findByPesel(pesel, currentUserId);
            return patientsWithTheSamePesel.size() == 0;
        }
        return false;
    }

    private boolean isPeselUnique(String pesel, int patientId) throws CurrentUserException {

        if (pesel.length() > 0) {
            int currentUserId = getCurrentUser().getId();
            List<Integer> patientsWithTheSamePesel = patientRepository.findByPesel(pesel, currentUserId);

            return patientsWithTheSamePesel.size() == 1 && patientsWithTheSamePesel.contains(patientId);
        }
        return false;
    }

    private boolean isPeselOfGoodLength(String pesel) {

        return pesel.length() == 11;
    }

    private void checkPatientOfCurrentUserOrThrow(int patientId) throws PatientWithIdNotExistsException, CurrentUserException {
        int currentUserId = getCurrentUser().getId();
        List<Integer> patients = patientRepository.getByIdAndCurrent(patientId, currentUserId);
        if (patients.size() == 0)
            throw new PatientWithIdNotExistsException(patientId);
    }


}
