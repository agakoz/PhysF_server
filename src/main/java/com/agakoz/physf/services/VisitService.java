package com.agakoz.physf.services;

import com.agakoz.physf.model.DTO.*;
import com.agakoz.physf.model.Photo;
import com.agakoz.physf.model.User;
import com.agakoz.physf.model.Visit;
import com.agakoz.physf.repositories.PhotoRepository;
import com.agakoz.physf.repositories.VisitRepository;
import com.agakoz.physf.services.exceptions.*;
import com.agakoz.physf.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDate.now;

@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final PatientService patientService;
    private final PhotoRepository photoRepository;

    @Autowired
    public VisitService(VisitRepository visitRepository, PatientService patientService, PhotoRepository photoRepository) {
        this.visitRepository = visitRepository;
        this.patientService = patientService;
        this.photoRepository = photoRepository;
    }

    public VisitWithPhotosDTO getById(int visitId) throws VisitNotExistsException {
        Optional<VisitDTO> visitOpt = visitRepository.retrieveVisitDTOById(visitId);
        if (visitOpt.isPresent()) {
            System.out.println("visit opt : " + visitOpt.get());
            VisitWithPhotosDTO visitWithPhotos = ObjectMapperUtils.map(visitOpt.get(), new VisitWithPhotosDTO());
            visitWithPhotos.setPhotos(photoRepository.getPhotosFromVisit(visitId));

            return visitWithPhotos;
        } else {
            throw new VisitNotExistsException();
        }
    }

    public List<VisitWithPhotosDTO> getAllVisits() throws CurrentUserException, NoVisitsException {
        List<VisitDTO> visitList = visitRepository.retrieveVisitDTOsByUserId(getCurrentUser().getId());
        List<VisitWithPhotosDTO> visitsWithPhotos = getPhotosForVisitList(visitList);
        return visitsWithPhotos;
    }


    public List<VisitWithPhotosDTO> getPatientVisits(int patientId) throws CurrentUserException, NoVisitsException, PatientWithIdNotExistsException {
        patientService.validatePatientIdForCurrentUser(patientId);
        List<VisitDTO> visitList = visitRepository.retrieveVisitDTOsByUserIdPatientId(getCurrentUser().getId(), patientId);
        List<VisitWithPhotosDTO> visitsWithPhotos = getPhotosForVisitList(visitList);
        return visitsWithPhotos;
    }


    //swagger nie obsluguje  multipartfile array- nie przesyła ich do kontrolera
    public void addNewVisit(VisitCreateUpdateDTO visitDTO, MultipartFile[] photos) throws IOException, CurrentUserException, PatientWithIdNotExistsException {

        Visit newVisit = ObjectMapperUtils.map(visitDTO, new Visit());
        newVisit.setUser(getCurrentUser());
        newVisit.setPatient(patientService.getPatient(visitDTO.getPatientId()));
        validateVisit(newVisit);
        visitRepository.save(newVisit);
        saveMultipartFilesAsPhotos(newVisit, photos);
    }


    public void update(int visitId, VisitCreateUpdateDTO updatedVisitDTO, MultipartFile[] photos) throws VisitNotExistsException, IOException {


        Optional<Visit> oldVisitOpt = visitRepository.findById(visitId);
        if (oldVisitOpt.isPresent()) {
            Visit updated = ObjectMapperUtils.map(updatedVisitDTO, oldVisitOpt.get());
            validateVisit(updated);
            visitRepository.save(updated);
            saveMultipartFilesAsPhotos(updated, photos);
        } else {
            throw new VisitWithIdNotExistsException(visitId);
        }

    }

    public void delete(int visitId) {
        Optional<Visit> visitToDeleteOpt = visitRepository.findById(visitId);
        if (visitToDeleteOpt.isPresent()) {
            Visit visitToDelete = visitToDeleteOpt.get();
            photoRepository.deletePhotosFromVisit(visitId);
            visitRepository.delete(visitToDelete);

        } else {
            throw new VisitNotExistsException();
        }
    }
//planned visits:

    public List<VisitPlanDTO> getAllPlannedVisits() throws CurrentUserException, NoVisitsException {
        List<VisitPlanDTO> plannedVisits = visitRepository.retrieveVisitPlanDTOsByUserId(getCurrentUser().getId());
        if (plannedVisits.size() > 0) {
            return plannedVisits;
        } else {
            throw new NoVisitsException();
        }
    }

    public void planVisit(VisitPlanCreateUpdateDTO visitPlanCreateUpdateDTO) throws IllegalArgumentException {
        patientService.validatePatientIdForCurrentUser(visitPlanCreateUpdateDTO.getPatientId());
        validate(visitPlanCreateUpdateDTO);

        Visit newVisit = ObjectMapperUtils.map(visitPlanCreateUpdateDTO, new Visit());

        newVisit.setUser(getCurrentUser());
        newVisit.setPatient(patientService.getPatient(visitPlanCreateUpdateDTO.getPatientId()));
        visitRepository.save(newVisit);
    }

    public void editPlannedVisit(int plannedVisitId, VisitPlanCreateUpdateDTO visitPlanCreateUpdateDTO) throws PlannedVisitWithIdNotExistsException {
        Optional<Visit> oldVisitOpt = visitRepository.findById(plannedVisitId);
        if (oldVisitOpt.isPresent()) {

            Visit oldVisit = oldVisitOpt.get();
            Visit updatedVisit = ObjectMapperUtils.map(visitPlanCreateUpdateDTO, oldVisit);
            validate(updatedVisit);
            visitRepository.save(updatedVisit);
        } else throw new PlannedVisitWithIdNotExistsException(plannedVisitId);
    }

    public List<VisitPlanDTO> getPlannedVisitsByDate(Date date) {
        List<VisitPlanDTO> plannedVisits = visitRepository.retrieveVisitPlanDTOsByDateUserId(date, getCurrentUser().getId());
        return plannedVisits;
    }

    private void saveMultipartFilesAsPhotos(Visit visit, MultipartFile[] photos) throws IOException {
        for (MultipartFile photo : photos) {
            Photo newPhoto = new Photo();
            newPhoto.setPhoto(photo.getBytes());
            newPhoto.setVisit(visit);
            photoRepository.save(newPhoto);
        }
    }


    //todo move it to separate class
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

    private List<VisitWithPhotosDTO> getPhotosForVisitList(List<VisitDTO> visits) throws NoVisitsException {
        if (visits.size() > 0) {
            List<VisitWithPhotosDTO> visitsWithPhotos = new ArrayList<>();
            visits.stream()
                    .forEach(visit -> visitsWithPhotos.add((ObjectMapperUtils.map(visit, new VisitWithPhotosDTO()))));
            visitsWithPhotos.stream()
                    .forEach(visit -> visit.setPhotos(photoRepository.getPhotosFromVisit(visit.getId())));
            return visitsWithPhotos;
        } else {
            throw new NoVisitsException();
        }
    }


    //TODO DO ZMIANY WALIDACJA
    private void validateVisit(Visit visit) {
        if (visit.getPatient() == null) throw new NullPatientException();
        patientService.validatePatientIdForCurrentUser(visit.getPatient().getId());
        if (visit.getDate() == null) throw new NullDateException();
        if (visit.getDate().isAfter(LocalDate.now())) throw new VisitAfterTodayException();

    }

    //TODO DO ZMIANY WALIDACJA
    private void validate(VisitPlanCreateUpdateDTO visit) {
        if (visit.getDate() == null) throw new NullDateException();
        if (visit.getDate().isBefore(now())) throw new PlannedVisitBeforeTodayException();
        if (visit.getStartTime() == null) throw new NullStartTimeException();
        if (visit.getEndTime() == null) throw new NullEndTimeException();
        if (visit.getStartTime().isAfter(visit.getEndTime())) throw new StartTimeAfterEndTimeException();
    }

    //TODO DO ZMIANY WALIDACJA
    private void validate(Visit visit) {
        if (visit.getDate() == null) throw new NullDateException();
        if (visit.getDate().isBefore(now())) throw new PlannedVisitBeforeTodayException();
        if (visit.getStartTime() == null) throw new NullStartTimeException();
        if (visit.getEndTime() == null) throw new NullEndTimeException();
        if (visit.getStartTime().isAfter(visit.getEndTime())) throw new StartTimeAfterEndTimeException();
    }
}
