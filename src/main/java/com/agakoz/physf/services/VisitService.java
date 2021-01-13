package com.agakoz.physf.services;

import com.agakoz.physf.model.DTO.FirstVisitPlanDTO;
import com.agakoz.physf.model.DTO.IncomingVisitDTO;
import com.agakoz.physf.model.DTO.VisitPlanWithTreatmentCycleDTO;
import com.agakoz.physf.model.Patient;
import com.agakoz.physf.model.TreatmentCycle;
import com.agakoz.physf.model.Visit;
import com.agakoz.physf.repositories.PhotoRepository;
import com.agakoz.physf.repositories.TreatmentCycleRepository;
import com.agakoz.physf.repositories.VisitRepository;
import com.agakoz.physf.services.exceptions.NoPatientsException;
import com.agakoz.physf.services.exceptions.NoVisitsException;
import com.agakoz.physf.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final PatientService patientService;
    private final PhotoRepository photoRepository;
    private final TreatmentCycleRepository treatmentCycleRepository;
    private final TreatmentCycleService treatmentCycleService;

    @Autowired
    public VisitService(
            VisitRepository visitRepository,
            PatientService patientService,
            PhotoRepository photoRepository,
            TreatmentCycleRepository treatmentCycleRepository,
            TreatmentCycleService treatmentCycleService) {
        this.visitRepository = visitRepository;
        this.patientService = patientService;
        this.photoRepository = photoRepository;
        this.treatmentCycleRepository = treatmentCycleRepository;
        this.treatmentCycleService = treatmentCycleService;
    }


    public List<IncomingVisitDTO> getIncomingVisits(int patientId) throws NoPatientsException {
        patientService.validatePatientIdForCurrentUser(patientId);
            List<IncomingVisitDTO> incomingVisits = visitRepository.findIncomingVisit(patientId, LocalDate.now());
            return incomingVisits;
    }

    @Transactional
    public int planFirstVisit(FirstVisitPlanDTO visitPlan, int patientId) {
        TreatmentCycle treatmentCycle = createTreatmentCycle(patientId);
        Visit visit = createVisitFromPlan(visitPlan, treatmentCycle);
        visitRepository.save(visit);
        return visit.getId();
    }

    @Transactional
    public int planNextVisit(VisitPlanWithTreatmentCycleDTO visitPlan) {

        Visit visit = ObjectMapperUtils.map(visitPlan, new Visit());
        Optional<TreatmentCycle> cycle = treatmentCycleRepository.findById(visitPlan.getTreatmentCycleId());
        cycle.ifPresent(visit::setTreatmentCycle);
        visitRepository.save(visit);
        return visit.getId();
    }

    private Visit createVisitFromPlan(FirstVisitPlanDTO firstVisitPlanDTO, TreatmentCycle treatmentCycle) {
        Visit visit = ObjectMapperUtils.map(firstVisitPlanDTO, new Visit());
        visit.setTreatmentCycle(treatmentCycle);
        return visit;
    }

    private TreatmentCycle createTreatmentCycle(int patientId) {
       Patient patient =  patientService.getPatient(patientId);
       TreatmentCycle treatmentCycle = new TreatmentCycle(UserService.getCurrentUser(), patient);
        treatmentCycleRepository.save(treatmentCycle);
        return treatmentCycle;
    }

    @Transactional
    public void cancelVisit(int visitId) throws NoVisitsException {
       Optional<Visit> visitToDelete= visitRepository.findById(visitId);
       if(visitToDelete.isEmpty()){
           throw new NoVisitsException();
       }
       TreatmentCycle treatmentCycle = visitToDelete.get().getTreatmentCycle();
       visitRepository.delete(visitToDelete.get());
       treatmentCycleService.deleteTreatmentCycleIfHasNoVisits(treatmentCycle);
    }

    @Transactional
    public void updateVisitPlan(int visitId, VisitPlanWithTreatmentCycleDTO newVisitPlan) throws NoVisitsException {
        Optional<Visit> visitToUpdate = visitRepository.findById(visitId);
        if(visitToUpdate.isEmpty()){
            throw new NoVisitsException();
        }
        //todo validate
        Visit oldVisitPlan = visitToUpdate.get();
        TreatmentCycle currentTreatmentCycle = oldVisitPlan.getTreatmentCycle();
        Visit updatedVisitPlan = ObjectMapperUtils.map(newVisitPlan, oldVisitPlan);
        if ( visitPlanIsForNewTreatmentCycle(newVisitPlan)) {
            TreatmentCycle newTreatmentCycle =  createTreatmentCycle(currentTreatmentCycle.getPatient().getId());
            updatedVisitPlan.setTreatmentCycle(newTreatmentCycle);
        }
        visitRepository.save(updatedVisitPlan);
        treatmentCycleService.deleteTreatmentCycleIfHasNoVisits(currentTreatmentCycle);


    }

    private boolean visitPlanIsForNewTreatmentCycle(VisitPlanWithTreatmentCycleDTO newVisitPlan) {
        return newVisitPlan.getTreatmentCycleId() == -1;
    }


//
//    public VisitWithPhotosDTO getById(int visitId) throws VisitNotExistsException {
//        Optional<VisitDTO> visitOpt = visitRepository.retrieveVisitDTOById(visitId);
//        if (visitOpt.isPresent()) {
//            System.out.println("visit opt : " + visitOpt.get());
//            VisitWithPhotosDTO visitWithPhotos = ObjectMapperUtils.map(visitOpt.get(), new VisitWithPhotosDTO());
//            visitWithPhotos.setPhotos(photoRepository.getPhotosFromVisit(visitId));
//
//            return visitWithPhotos;
//        } else {
//            throw new VisitNotExistsException();
//        }
//    }
//
//    public List<VisitWithPhotosDTO> getAllVisits() throws UserException, NoVisitsException {
//        List<VisitDTO> visitList = visitRepository.retrieveVisitDTOsByUserId(getCurrentUser().getId());
//        List<VisitWithPhotosDTO> visitsWithPhotos = getPhotosForVisitList(visitList);
//        return visitsWithPhotos;
//    }
//
//
//    public List<VisitWithPhotosDTO> getPatientVisits(int patientId) throws UserException, NoVisitsException, PatientWithIdNotExistsException {
//        patientService.validatePatientIdForCurrentUser(patientId);
//        List<VisitDTO> visitList = visitRepository.retrieveVisitDTOsByUserIdPatientId(getCurrentUser().getId(), patientId);
//        List<VisitWithPhotosDTO> visitsWithPhotos = getPhotosForVisitList(visitList);
//        return visitsWithPhotos;
//    }
//
//
//    //swagger nie obsluguje  multipartfile array- nie przesyła ich do kontrolera
//    public void addNewVisit(VisitCreateUpdateDTO visitDTO, MultipartFile[] photos) throws IOException, UserException, PatientWithIdNotExistsException {
//
//        Visit newVisit = ObjectMapperUtils.map(visitDTO, new Visit());
////        newVisit.setUser(getCurrentUser());
////        newVisit.setPatient(patientService.getPatient(visitDTO.getPatientId()));
//        validateVisit(newVisit);
//        visitRepository.save(newVisit);
//        saveMultipartFilesAsPhotos(newVisit, photos);
//    }
//
//
//    public void update(int visitId, VisitCreateUpdateDTO updatedVisitDTO, MultipartFile[] photos) throws VisitNotExistsException, IOException {
//
//
//        Optional<Visit> oldVisitOpt = visitRepository.findById(visitId);
//        if (oldVisitOpt.isPresent()) {
//            Visit updated = ObjectMapperUtils.map(updatedVisitDTO, oldVisitOpt.get());
//            validateVisit(updated);
//            visitRepository.save(updated);
//            saveMultipartFilesAsPhotos(updated, photos);
//        } else {
//            throw new VisitWithIdNotExistsException(visitId);
//        }
//
//    }
//
//    public void delete(int visitId) {
//        Optional<Visit> visitToDeleteOpt = visitRepository.findById(visitId);
//        if (visitToDeleteOpt.isPresent()) {
//            Visit visitToDelete = visitToDeleteOpt.get();
//            photoRepository.deletePhotosFromVisit(visitId);
//            visitRepository.delete(visitToDelete);
//
//        } else {
//            throw new VisitNotExistsException();
//        }
//    }
////planned visits:
//
//    public List<VisitPlanDTO> getAllPlannedVisits() throws UserException, NoVisitsException {
//        List<VisitPlanDTO> plannedVisits = visitRepository.retrieveVisitPlanDTOsByUserId(getCurrentUser().getId());
//        if (plannedVisits.size() > 0) {
//            return plannedVisits;
//        } else {
//            throw new NoVisitsException();
//        }
//    }
//
//    public void planVisit(VisitPlanCreateUpdateDTO visitPlanCreateUpdateDTO) throws IllegalArgumentException {
//        patientService.validatePatientIdForCurrentUser(visitPlanCreateUpdateDTO.getPatientId());
//        validate(visitPlanCreateUpdateDTO);
//
//        Visit newVisit = ObjectMapperUtils.map(visitPlanCreateUpdateDTO, new Visit());
//
////        newVisit.setUser(getCurrentUser());
////        newVisit.setPatient(patientService.getPatient(visitPlanCreateUpdateDTO.getPatientId()));
//        visitRepository.save(newVisit);
//    }
//
//    public void editPlannedVisit(int plannedVisitId, VisitPlanCreateUpdateDTO visitPlanCreateUpdateDTO) throws PlannedVisitWithIdNotExistsException {
//        Optional<Visit> oldVisitOpt = visitRepository.findById(plannedVisitId);
//        if (oldVisitOpt.isPresent()) {
//
//            Visit oldVisit = oldVisitOpt.get();
//            Visit updatedVisit = ObjectMapperUtils.map(visitPlanCreateUpdateDTO, oldVisit);
//            validate(updatedVisit);
//            visitRepository.save(updatedVisit);
//        } else throw new PlannedVisitWithIdNotExistsException(plannedVisitId);
//    }
//
//    public List<VisitPlanDTO> getPlannedVisitsByDate(Date date) {
//        List<VisitPlanDTO> plannedVisits = visitRepository.retrieveVisitPlanDTOsByDateUserId(date, getCurrentUser().getId());
//        return plannedVisits;
//    }
//
//    private void saveMultipartFilesAsPhotos(Visit visit, MultipartFile[] photos) throws IOException {
//        for (MultipartFile photo : photos) {
//            Photo newPhoto = new Photo();
//            newPhoto.setPhoto(photo.getBytes());
//            newPhoto.setVisit(visit);
//            photoRepository.save(newPhoto);
//        }
//    }
//
//
//    //todo move it to separate class
//    private User getCurrentUser() throws UserException {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        User currentUser;
//        if (principal instanceof UserDetails) {
//            currentUser = ((User) (principal));
//
//        } else {
//            throw new UserException("");
//
//        }
//        return currentUser;
//    }
//
//    private List<VisitWithPhotosDTO> getPhotosForVisitList(List<VisitDTO> visits) throws NoVisitsException {
//        if (visits.size() > 0) {
//            List<VisitWithPhotosDTO> visitsWithPhotos = new ArrayList<>();
//            visits.stream()
//                    .forEach(visit -> visitsWithPhotos.add((ObjectMapperUtils.map(visit, new VisitWithPhotosDTO()))));
//            visitsWithPhotos.stream()
//                    .forEach(visit -> visit.setPhotos(photoRepository.getPhotosFromVisit(visit.getId())));
//            return visitsWithPhotos;
//        } else {
//            throw new NoVisitsException();
//        }
//    }
//
//
//    //TODO DO ZMIANY WALIDACJA
//    private void validateVisit(Visit visit) {
////        if (visit.getPatient() == null) throw new NullPatientException();
////        patientService.validatePatientIdForCurrentUser(visit.getPatient().getId());
//        if (visit.getDate() == null) throw new NullDateException();
//        if (visit.getDate().isAfter(LocalDate.now())) throw new VisitAfterTodayException();
//
//    }
//
//    //TODO DO ZMIANY WALIDACJA
//    private void validate(VisitPlanCreateUpdateDTO visit) {
//        if (visit.getDate() == null) throw new NullDateException();
//        if (visit.getDate().isBefore(now())) throw new PlannedVisitBeforeTodayException();
//        if (visit.getStartTime() == null) throw new NullStartTimeException();
//        if (visit.getEndTime() == null) throw new NullEndTimeException();
//        if (visit.getStartTime().isAfter(visit.getEndTime())) throw new StartTimeAfterEndTimeException();
//    }
//
//    //TODO DO ZMIANY WALIDACJA
//    private void validate(Visit visit) {
//        if (visit.getDate() == null) throw new NullDateException();
//        if (visit.getDate().isBefore(now())) throw new PlannedVisitBeforeTodayException();
//        if (visit.getStartTime() == null) throw new NullStartTimeException();
//        if (visit.getEndTime() == null) throw new NullEndTimeException();
//        if (visit.getStartTime().isAfter(visit.getEndTime())) throw new StartTimeAfterEndTimeException();
//    }
}
