package com.agakoz.physf.services;

import com.agakoz.physf.model.*;
import com.agakoz.physf.model.DTO.*;
import com.agakoz.physf.repositories.*;
import com.agakoz.physf.security.SecurityUtils;
import com.agakoz.physf.services.exceptions.*;
import com.agakoz.physf.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.file.NoSuchFileException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final PatientService patientService;
    private final PhotoRepository photoRepository;
    private final TreatmentCycleRepository treatmentCycleRepository;
    private final TreatmentCycleService treatmentCycleService;
    private final ExternalAttachmentRepository externalAttachmentRepository;
    private final FileRepository fileRepository;
    private ExternalAttachmentService externalAttachmentService;

    @Autowired
    public VisitService(
            VisitRepository visitRepository,
            PatientService patientService,
            PhotoRepository photoRepository,
            TreatmentCycleRepository treatmentCycleRepository,
            TreatmentCycleService treatmentCycleService, ExternalAttachmentRepository externalAttachmentRepository, FileRepository fileRepository, ExternalAttachmentService externalAttachmentService) {
        this.visitRepository = visitRepository;
        this.patientService = patientService;
        this.photoRepository = photoRepository;
        this.treatmentCycleRepository = treatmentCycleRepository;
        this.treatmentCycleService = treatmentCycleService;
        this.externalAttachmentRepository = externalAttachmentRepository;
        this.fileRepository = fileRepository;
        this.externalAttachmentService = externalAttachmentService;
    }

    public FinishedVisitDTO getFinishedVisitInfo(int visitId) throws NoVisitsException {
        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new UserException("Current user login not found"));
        Optional<FinishedVisitDTO> visit = visitRepository.retrieveVisitAsFinishedVisitDTO(visitId, currentUsername);
        if (visit.isEmpty()) {
            throw new NoVisitsException();
        }
        return visit.get();
    }


    public List<VisitPlanDTO> getIncomingVisits(int patientId) throws NoPatientsException {
        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new UserException("Current user login not found"));
        patientService.validatePatientIdForCurrentUser(patientId);
        List<VisitPlanDTO> incomingVisits = visitRepository.findIncomingVisit(patientId, LocalDate.now(), currentUsername);
        return incomingVisits;
    }

    @Transactional(rollbackOn = Exception.class)
    public int planFirstVisit(FirstVisitPlanDTO visitPlan, int patientId) {

        TreatmentCycle treatmentCycle = createTreatmentCycle(patientId);
        Visit visit = createVisitFromPlan(visitPlan, treatmentCycle);
        visitRepository.save(visit);
        return visit.getId();
    }

    @Transactional(rollbackOn = Exception.class)
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
        Patient patient = patientService.getPatient(patientId);
        TreatmentCycle treatmentCycle = new TreatmentCycle(UserService.getCurrentUser(), patient);
        treatmentCycleRepository.save(treatmentCycle);
        return treatmentCycle;
    }

    @Transactional(rollbackOn = Exception.class)
    public void cancelVisit(int visitId) throws NoVisitsException {
        Optional<Visit> visitToDelete = visitRepository.findById(visitId);
        if (visitToDelete.isEmpty()) {
            throw new NoVisitsException();
        }
        if (visitToDelete.get().isFinished()) {
            throw new IllegalArgumentException("Nie można odwołać wizyty, która już się odbyła.");
        }
        TreatmentCycle treatmentCycle = visitToDelete.get().getTreatmentCycle();
        visitRepository.delete(visitToDelete.get());
        treatmentCycleService.deleteTreatmentCycleIfHasNoVisits(treatmentCycle);
    }

    @Transactional(rollbackOn = Exception.class)
    public int updateVisitPlan(int visitId, VisitPlanWithTreatmentCycleDTO newVisitPlan) throws NoVisitsException {
        Optional<Visit> visitToUpdate = visitRepository.findById(visitId);
        if (visitToUpdate.isEmpty()) {
            throw new NoVisitsException();
        }
        Visit oldVisitPlan = visitToUpdate.get();

        Visit updatedVisitPlan = ObjectMapperUtils.map(newVisitPlan, oldVisitPlan);
        TreatmentCycle currentTreatmentCycle = oldVisitPlan.getTreatmentCycle();
        if (newVisitPlan.getTreatmentCycleId() != currentTreatmentCycle.getId()) {
            if (visitPlanIsForNewTreatmentCycle(newVisitPlan)) {
                TreatmentCycle newTreatmentCycle = createTreatmentCycle(currentTreatmentCycle.getPatient().getId());
                updatedVisitPlan.setTreatmentCycle(newTreatmentCycle);
            } else {
                Optional<TreatmentCycle> treatmentCycle = treatmentCycleRepository.findById(newVisitPlan.getTreatmentCycleId());
                if (treatmentCycle.isEmpty()) {
                    throw new VisitNotExistsException();
                }
                updatedVisitPlan.setTreatmentCycle(treatmentCycle.get());
            }
        }
        visitRepository.save(updatedVisitPlan);
        treatmentCycleService.deleteTreatmentCycleIfHasNoVisits(currentTreatmentCycle);

        return updatedVisitPlan.getId();
    }

    private boolean visitPlanIsForNewTreatmentCycle(VisitPlanWithTreatmentCycleDTO newVisitPlan) {
        return newVisitPlan.getTreatmentCycleId() == -1;
    }

    public List<VisitCalendarEvent> getCalendarEventsFromUser() {

        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new UserException("Current user login not found"));
        List<VisitCalendarEvent> events = visitRepository.retrieveAllVisitsAsCalendarEvent(currentUsername);
        return events;
    }

    public VisitCalendarEvent getCalendarEvent(int visitId) throws NoVisitsException {
        Optional<VisitCalendarEvent> event = visitRepository.retrieveVisitAsCalendarEvent(visitId);
        if (event.isEmpty()) {
            throw new NoVisitsException();
        }
        return event.get();
    }

    public VisitPlanDTO getIncomingVisit(int visitId) throws NoVisitsException {
        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new UserException("Current user login not found"));
        Optional<VisitPlanDTO> incomingVisits = visitRepository.findIncomingVisit(visitId, currentUsername);
        if (incomingVisits.isEmpty()) {
            throw new NoVisitsException();
        }
        return incomingVisits.get();
    }

    @Transactional(rollbackOn = Exception.class)
    public int planVisitForNewPatient(Map<String, Object> visitPlan) {
        int patientId = patientService.createNewPatientAndGetId(visitPlan.get("patient"));
        TreatmentCycle treatmentCycle = createTreatmentCycle(patientId);
        Visit newVisitPlan = new Visit();
        Object visitPlanTime = visitPlan.get("visit");
        newVisitPlan.setDate(getVisitDate(visitPlanTime));
        newVisitPlan.setStartTime(getVisitStartTime(visitPlanTime));
        newVisitPlan.setEndTime(getVisitEndTime(visitPlanTime));
        newVisitPlan.setTreatmentCycle(treatmentCycle);
        visitRepository.save(newVisitPlan);
        return newVisitPlan.getId();
    }

    @Transactional(rollbackOn = Exception.class)
    public int finishVisit(FinishVisitWrapper detailsWrapper) throws NoVisitsException, NoSuchFileException {
        VisitToFinishDTO visitDetails = detailsWrapper.getVisit();
        TreatmentCycleInfoDTO treatmentCycleDetails = detailsWrapper.getTreatmentCycle();
        if (visitDetails.getTreatmentCycleId() != treatmentCycleDetails.getId()) {
            throw new IllegalArgumentException();
        }
        Visit finishedVisit = createFinishedVisitFromDetails(visitDetails);
        TreatmentCycle treatmentCycle = getTreatmentCycle(treatmentCycleDetails);
        List<TreatmentCycleAttachmentDTO> attachments = detailsWrapper.getAttachments();
        List<Integer> updatedAttachmentIds = attachments.stream().map(TreatmentCycleAttachmentDTO::getId).filter(id -> id > -1).collect(Collectors.toList());
        externalAttachmentService.updateAssignedAttachmentsDeleteOld(treatmentCycle.getId(), updatedAttachmentIds);
        finishedVisit.setTreatmentCycle(treatmentCycle);
        visitRepository.save(finishedVisit);
        treatmentCycleRepository.save(treatmentCycle);
        for (TreatmentCycleAttachmentDTO a : attachments) {
            externalAttachmentService.createOrUpdateAttachment(a, treatmentCycle);
        }

        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new UserException("Current user login not found"));
        int finishedVisitId = visitRepository.getLastVisit(currentUsername);
        return finishedVisitId;
    }

//    private void createAttachmentAndAssignAttachmentToTreatmentCycleAndFile(TreatmentCycleAttachmentDTO attachmentDetails, TreatmentCycle treatmentCycle) throws NoSuchFileException {
//        ExternalAttachment attachment;
//        if (attachmentDetails.getId() == -1) {
//            attachment = new ExternalAttachment();
//            attachment = ObjectMapperUtils.map(attachmentDetails, attachment);
//            attachment.setId(getIdForNewAttachment());
//        } else {
//            Optional<ExternalAttachment> retrievedAttachment = externalAttachmentRepository.findById(attachmentDetails.getId());
//            if (retrievedAttachment.isPresent()) {
//                attachment = retrievedAttachment.get();
//                attachment = ObjectMapperUtils.map(attachmentDetails, attachment);
//            } else {
//                throw new IllegalArgumentException();
//            }
//        }
//        if (attachmentDetails.getFileId() > -1) {
//            Optional<UploadedFile> uploadedFile = fileRepository.findById(attachmentDetails.getFileId());
//            if (uploadedFile.isEmpty()) {
//                throw new NoSuchFileException("");
//            } else {
//                attachment.setFile(uploadedFile.get());
//            }
//        }
//        attachment.setTreatmentCycle(treatmentCycle);
//        externalAttachmentRepository.save(attachment);
//    }

//    private int getIdForNewAttachment() {
//        Optional<Integer> idOpt = externalAttachmentRepository.getLastId();
//        return idOpt.map(integer -> integer + 1).orElse(0);
//    }

    private TreatmentCycle getTreatmentCycle(TreatmentCycleInfoDTO treatmentCycleDetails) {
        TreatmentCycle treatmentCycle;
        int treatmentCycleId = treatmentCycleDetails.getId();
        if (treatmentCycleId == -1) {
            treatmentCycle = createTreatmentCycle(treatmentCycleDetails.getPatientId());

        } else {
            Optional<TreatmentCycle> treatmentCycleOptional = treatmentCycleRepository.findById(treatmentCycleId);
            if (treatmentCycleOptional.isEmpty()) {
                throw new IllegalArgumentException();
            }
            treatmentCycle = treatmentCycleOptional.get();
        }
        treatmentCycle = ObjectMapperUtils.map(treatmentCycleDetails, treatmentCycle);
        return treatmentCycle;
    }

    private Visit createFinishedVisitFromDetails(VisitToFinishDTO visitDetails) throws NoVisitsException {
        int visitId = visitDetails.getId();
        Optional<Visit> visitPlanOptional = visitRepository.findById(visitId);
        Visit visitPlan;
        if (isVisitWithoutPlan(visitId)) {
            visitPlan = new Visit();

        } else {
            if (visitPlanOptional.isEmpty()) {
                throw new NoVisitsException();
            } else if (visitPlanOptional.get().isFinished()) {
                throw new VisitAlreadyFinishedException("Nie można rozpocząć zakończonej wizyty");
            } else {
                visitPlan = visitPlanOptional.get();
            }
        }

        Visit finishedVisit = ObjectMapperUtils.map(visitDetails, visitPlan);
        finishedVisit.setFinished(true);
        return finishedVisit;
    }

    private LocalTime getVisitEndTime(Object visitDetails) {
        if (((LinkedHashMap) visitDetails).get("endTime") == null) {
            return null;
        }
        return LocalTime.parse((String) ((LinkedHashMap) visitDetails).get("endTime"), DateTimeFormatter.ofPattern("HH:mm"));

    }

    private LocalTime getVisitStartTime(Object visitDetails) {
        if (((LinkedHashMap) visitDetails).get("startTime") == null) {
            return null;
        }
        return LocalTime.parse((String) ((LinkedHashMap) visitDetails).get("startTime"), DateTimeFormatter.ofPattern("HH:mm"));

    }

    private boolean isVisitWithoutPlan(int visitId) {
        return visitId == -1;
    }

    private LocalDate getVisitDate(Object visitDetails) {
        if (((LinkedHashMap) visitDetails).get("date") == null) {
            return null;
        }
        return LocalDate.parse((String) ((LinkedHashMap) visitDetails).get("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private LocalDate getTreatmentCycleDate(Object treatmentCycleDetails) {
        if (((LinkedHashMap) treatmentCycleDetails).get("injuryDate") == null) {
            return null;
        }
        return LocalDate.parse((String) ((LinkedHashMap) treatmentCycleDetails).get("injuryDate"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private int getPatientId(Object visitDetails) {
        return (int) ((LinkedHashMap) visitDetails).get("patientId");

    }

    private int getVisitId(Object visitDetails) {
        return (int) ((LinkedHashMap) visitDetails).get("id");
    }

    private int getVisitTreatmentCycleId(Object visit) {
        if (((LinkedHashMap) visit).get("treatmentCycleId") == null) {
            return -1;
        }
        return (int) ((LinkedHashMap) visit).get("treatmentCycleId");
    }

    public boolean isVisitPlannedInGivenTime(VisitDateTimeInfo visitDateTime) {
        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new UserException("Current user login not found"));
        boolean isVisitPlanned =
                visitRepository.isVisitPlannedForGivenDateAndTime(currentUsername, visitDateTime.getId(), visitDateTime.getDate(), visitDateTime.getStartTime(), visitDateTime.getEndTime());
//        int id = visitRepository.visitPlannedForGivenDateAndTime(currentUsername, visitDateTime.getVisitId(), visitDateTime.getDate(), visitDateTime.getStartTime(), visitDateTime.getEndTime());
        return isVisitPlanned;
    }

    public List<FinishedVisitDTO> getAllFinishedVisitsDataFromTreatmentCycle(int treatmentCycleId) {
        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new UserException("Current user login not found"));
        return visitRepository.retrieveAllFinishedVisitsAsDTOByTreatmentCycleId(currentUsername, treatmentCycleId);
    }


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
