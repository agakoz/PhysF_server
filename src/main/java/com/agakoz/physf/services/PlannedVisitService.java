package com.agakoz.physf.services;

import com.agakoz.physf.model.DTO.PlannedVisitCreateDTO;
import com.agakoz.physf.model.DTO.PlannedVisitDTO;
import com.agakoz.physf.model.PlannedVisit;
import com.agakoz.physf.model.User;
import com.agakoz.physf.repositories.PlannedVisitRepository;
import com.agakoz.physf.services.exceptions.*;
import com.agakoz.physf.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.time.LocalDate.now;

@Service
public class PlannedVisitService {

    private PlannedVisitRepository plannedVisitRepository;
    private PatientService patientService;

    @Autowired
    public PlannedVisitService(PlannedVisitRepository plannedVisitRepository, PatientService patientService) {
        this.plannedVisitRepository = plannedVisitRepository;
        this.patientService = patientService;
    }

    public List<PlannedVisitDTO> getAllPlannedVisits() throws CurrentUserException, NoVisitsException {
        List<PlannedVisitDTO> plannedVisits = plannedVisitRepository.retrieveAllAsDTOByUserId(getCurrentUser().getId());
        if (plannedVisits.size() > 0) {
            return plannedVisits;
        } else {
            throw new NoVisitsException();
        }
    }

    public void planVisit(PlannedVisitCreateDTO plannedVisitCreateDTO) throws IllegalArgumentException {
        patientService.validatePatientIdForCurrentUser(plannedVisitCreateDTO.getPatientId());
        validate(plannedVisitCreateDTO);

        PlannedVisit newVisit = ObjectMapperUtils.map(plannedVisitCreateDTO, new PlannedVisit());

        newVisit.setUser(getCurrentUser());
        newVisit.setPatient(patientService.getPatient(plannedVisitCreateDTO.getPatientId()));
        plannedVisitRepository.save(newVisit);
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


    public void update(int plannedVisitId, PlannedVisitCreateDTO plannedVisitCreateDTO) throws PlannedVisitWithIdNotExistsException {
        Optional<PlannedVisit> oldVisitOpt = plannedVisitRepository.getById(plannedVisitId);
        if (oldVisitOpt.isPresent()) {

            PlannedVisit oldVisit = oldVisitOpt.get();
            PlannedVisit updatedVisit = ObjectMapperUtils.map(plannedVisitCreateDTO, oldVisit);
            validate(updatedVisit);
            plannedVisitRepository.save(updatedVisit);
        } else throw new PlannedVisitWithIdNotExistsException(plannedVisitId);
    }

    public void delete(int plannedVisitId) throws PlannedVisitWithIdNotExistsException {
        Optional<PlannedVisit> plannedVisitOpt = plannedVisitRepository.getById(plannedVisitId);
        if (plannedVisitOpt.isPresent()) {
            plannedVisitRepository.delete(plannedVisitOpt.get());
        } else throw new PlannedVisitWithIdNotExistsException(plannedVisitId);
    }

    private void validate(PlannedVisitCreateDTO visit) {
        if (visit.getDate() == null) throw new NullDateException();
        if (visit.getDate().isBefore(now())) throw new PlannedVisitBeforeTodayException();
        if (visit.getStartTime() == null) throw new NullStartTimeException();
        if (visit.getEndTime() == null) throw new NullEndTimeException();
        if (visit.getStartTime().isAfter(visit.getEndTime())) throw new StartTimeAfterEndTimeException();
    }

    private void validate(PlannedVisit visit) {
        if (visit.getDate() == null) throw new NullDateException();
        if (visit.getDate().isBefore(now())) throw new PlannedVisitBeforeTodayException();
        if (visit.getStartTime() == null) throw new NullStartTimeException();
        if (visit.getEndTime() == null) throw new NullEndTimeException();
        if (visit.getStartTime().isAfter(visit.getEndTime())) throw new StartTimeAfterEndTimeException();
    }


}