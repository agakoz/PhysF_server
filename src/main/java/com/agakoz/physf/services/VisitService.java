package com.agakoz.physf.services;

import com.agakoz.physf.model.DTO.VisitCreateUpdateDTO;
import com.agakoz.physf.model.DTO.VisitDTO;
import com.agakoz.physf.model.DTO.VisitWithPhotosDTO;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        Optional<VisitDTO> visitOpt = visitRepository.retrieveDTOById(visitId);
        if (visitOpt.isPresent()) {
            VisitWithPhotosDTO visitWithPhotos = ObjectMapperUtils.map(visitOpt.get(), new VisitWithPhotosDTO());
            visitWithPhotos.setPhotos(photoRepository.getPhotosFromVisit(visitId));
            return visitWithPhotos;
        } else {
            throw new VisitNotExistsException();
        }
    }

    public List<VisitWithPhotosDTO> getAllVisits() throws CurrentUserException, NoVisitsException {
        List<VisitDTO> visitList = visitRepository.findAllByUserId(getCurrentUser().getId());
        List<VisitWithPhotosDTO> visitsWithPhotos = getPhotosForVisitList(visitList);
        return visitsWithPhotos;
    }


    public List<VisitWithPhotosDTO> getPatientVisits(int patientId) throws CurrentUserException, NoVisitsException, PatientWithIdNotExistsException {
        patientService.validatePatientIdForCurrentUser(patientId);
        List<VisitDTO> visitList = visitRepository.findByUserAndPatientId(getCurrentUser().getId(), patientId);
        List<VisitWithPhotosDTO> visitsWithPhotos = getPhotosForVisitList(visitList);
        return visitsWithPhotos;
    }

    //swagger nie obsluguje  multipartfile array- nie przesyła ich do kontrolera
    public void addVisit(VisitCreateUpdateDTO visitDTO, MultipartFile[] photos) throws IOException, CurrentUserException, PatientWithIdNotExistsException {

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

    private void saveMultipartFilesAsPhotos(Visit visit, MultipartFile[] photos) throws IOException {
        for (MultipartFile photo : photos) {
            Photo newPhoto = new Photo();
            newPhoto.setPhoto(photo.getBytes());
            newPhoto.setVisit(visit);
            photoRepository.save(newPhoto);
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

    private void validateVisit(Visit visit) {
        if (visit.getPatient() == null) throw new NullPatientException();
        patientService.validatePatientIdForCurrentUser(visit.getPatient().getId());
        if (visit.getDate() == null) throw new NullDateException();
        if (visit.getDate().isAfter(LocalDate.now())) throw new VisitAfterTodayException();


    }
}
