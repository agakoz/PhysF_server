//package com.agakoz.physf.services;
//
//import com.agakoz.physf.model.DTO.VisitPlanCreateUpdateDTO;
//import com.agakoz.physf.model.DTO.VisitPlanDTO;
//import com.agakoz.physf.model.PlannedVisit;
//import com.agakoz.physf.model.User;
//import com.agakoz.physf.model.Visit;
//import com.agakoz.physf.repositories.PlannedVisitRepository;
//import com.agakoz.physf.repositories.VisitRepository;
//import com.agakoz.physf.services.exceptions.*;
//import com.agakoz.physf.utils.ObjectMapperUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import java.sql.Date;
//import java.util.List;
//import java.util.Optional;
//
//import static java.time.LocalDate.now;
//
//@Service
//public class PlannedVisitService {
//
//    private VisitRepository visitRepository;
//    private PatientService patientService;
//
//    @Autowired
//    public PlannedVisitService(VisitRepository visitRepository, PatientService patientService) {
//        this.visitRepository = visitRepository;
//        this.patientService = patientService;
//    }
//
//    public List<VisitPlanDTO> getAllPlannedVisits() throws CurrentUserException, NoVisitsException {
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
//        newVisit.setUser(getCurrentUser());
//        newVisit.setPatient(patientService.getPatient(visitPlanCreateUpdateDTO.getPatientId()));
//        visitRepository.save(newVisit);
//    }
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
//    }
////    private User getCurrentUser() throws CurrentUserException {
////        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
////
////        User currentUser;
////        if (principal instanceof UserDetails) {
////            currentUser = ((User) (principal));
////
////        } else {
////            throw new CurrentUserException();
////
////        }
////        return currentUser;
////    }
//
//
//
//
//    public void delete(int plannedVisitId) throws PlannedVisitWithIdNotExistsException {
//        Optional<PlannedVisit> plannedVisitOpt = visitRepository.getById(plannedVisitId);
//        if (plannedVisitOpt.isPresent()) {
//            visitRepository.delete(plannedVisitOpt.get());
//        } else throw new PlannedVisitWithIdNotExistsException(plannedVisitId);
//    }
//
//    private void validate(VisitPlanCreateUpdateDTO visit) {
//        if (visit.getDate() == null) throw new NullDateException();
//        if (visit.getDate().isBefore(now())) throw new PlannedVisitBeforeTodayException();
//        if (visit.getStartTime() == null) throw new NullStartTimeException();
//        if (visit.getEndTime() == null) throw new NullEndTimeException();
//        if (visit.getStartTime().isAfter(visit.getEndTime())) throw new StartTimeAfterEndTimeException();
//    }
//
//    private void validate(Visit visit) {
//        if (visit.getDate() == null) throw new NullDateException();
//        if (visit.getDate().isBefore(now())) throw new PlannedVisitBeforeTodayException();
//        if (visit.getStartTime() == null) throw new NullStartTimeException();
//        if (visit.getEndTime() == null) throw new NullEndTimeException();
//        if (visit.getStartTime().isAfter(visit.getEndTime())) throw new StartTimeAfterEndTimeException();
//    }
//
//
//
//}