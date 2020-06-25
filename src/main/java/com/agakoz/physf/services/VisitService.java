package com.agakoz.physf.services;

import com.agakoz.physf.model.DTO.VisitCreateUpdateDTO;
import com.agakoz.physf.model.DTO.VisitDTO;
import com.agakoz.physf.model.User;
import com.agakoz.physf.model.Visit;
import com.agakoz.physf.repositories.VisitRepository;
import com.agakoz.physf.services.exceptions.*;
import com.agakoz.physf.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VisitService {

    private VisitRepository visitRepository;
    private PatientService patientService;

    @Autowired
    public VisitService(VisitRepository visitRepository, PatientService patientService) {
        this.visitRepository = visitRepository;
        this.patientService = patientService;
    }

    public List<VisitDTO> getAllVisits() throws CurrentUserException, NoVisitsException {
        List<VisitDTO> visits = visitRepository.findAllByUserId(getCurrentUser().getId());
        if (visits.size() > 0) {
            return visits;
        } else {
            throw new NoVisitsException();
        }
    }


    public List<VisitDTO> getPatientVisits(int patientId) throws CurrentUserException, NoVisitsException, PatientWithIdNotExistsException {
        patientService.checkPatientOfCurrentUserOrThrow(patientId);
        List<VisitDTO> visits = visitRepository.findByUserAndPatientId(getCurrentUser().getId(), patientId);
        if (visits.size() > 0) {
            return visits;
        } else {
            throw new NoVisitsException();
        }
    }

    public void addVisit(VisitCreateUpdateDTO visitDTO) throws CurrentUserException, PatientWithIdNotExistsException {
        patientService.checkPatientOfCurrentUserOrThrow(visitDTO.getPatientId());
        //TODO visitValidation

        Visit newVisit = ObjectMapperUtils.map(visitDTO, new Visit());
        newVisit.setUser(getCurrentUser());
        newVisit.setPatient(patientService.getPatient(visitDTO.getPatientId()));
        visitRepository.save(newVisit);
    }

    public void update(int visitId, VisitCreateUpdateDTO visitDTO) {
        patientService.checkPatientOfCurrentUserOrThrow(visitDTO.getPatientId());
        //TODO visitValidation
        Optional<Visit> oldVisitOpt = visitRepository.findById(visitId);
        if(oldVisitOpt.isPresent()){
            Visit oldVisit = oldVisitOpt.get();
            Visit updated = ObjectMapperUtils.map(visitDTO, oldVisit);
            visitRepository.save(updated);
        } else {
            throw new VisitWithIdNotExistsException(visitId);
        }

    }
    public void delete(int id) {
        Optional<Visit> visitToDeleteOpt = visitRepository.findById(id);
        if(visitToDeleteOpt.isPresent()){
            Visit visitToDelete = visitToDeleteOpt.get();
            visitRepository.delete(visitToDelete);
        } else {
            throw new VisitNotExistsException();
        }
    }

    //todo move it to seperate class
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


    public VisitDTO getById(int visitId) throws VisitNotExistsException{
        Optional<VisitDTO> visitOpt = visitRepository.retrieveDTOById(visitId);
        if(visitOpt.isPresent()){
            return visitOpt.get();
        }else {
            throw new VisitNotExistsException();
        }
    }
}
