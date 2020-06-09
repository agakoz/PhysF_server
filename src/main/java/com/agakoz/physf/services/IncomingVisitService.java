//package com.agakoz.physf.services;
//
//import com.agakoz.physf.model.DTO.IncomingVisitDTO;
//import com.agakoz.physf.model.User;
//import com.agakoz.physf.repositories.IncomingVisitRepository;
//import com.agakoz.physf.services.exceptions.CurrentUserException;
//import com.agakoz.physf.services.exceptions.NoVisitsException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import java.util.Date;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class IncomingVisitService {
//
//    private IncomingVisitRepository incomingVisitRepository;
//
//    @Autowired
//    public IncomingVisitService(IncomingVisitRepository incomingVisitRepository) {
//        this.incomingVisitRepository = incomingVisitRepository;
//    }
//
//    public List<IncomingVisitDTO> getAllPlannedVisits() throws CurrentUserException, NoVisitsException{
//        List<IncomingVisitDTO> plannedVisits = incomingVisitRepository.findAllByUserId(getCurrentUser().getId());
//        if(plannedVisits.size()>0 ){
//            return plannedVisits;
//        }else{
//            throw new NoVisitsException("planned");
//        }
//    }
//    public List<IncomingVisitDTO> getAllIncomingVisits() throws CurrentUserException, NoVisitsException{
//        List<IncomingVisitDTO> plannedVisits = incomingVisitRepository.findAllByUserId(getCurrentUser().getId());
//        if(plannedVisits.size()>0 ){
//            List<IncomingVisitDTO> incomingVisitDTO = plannedVisits.stream()
//                    .filter(IncomingVisitDTO -> IncomingVisitDTO.getDate().after(new Date()))
//                    .collect(Collectors.toList());
//            return incomingVisitDTO;
//        }else{
//            throw new NoVisitsException();
//        }
//    }
//
//
//
//    private User getCurrentUser() throws CurrentUserException {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//        User currentUser;
//        if (principal instanceof UserDetails) {
//            currentUser = ((User) (principal));
//
//        } else {
//            throw new CurrentUserException();
//
//        }
//        return currentUser;
//    }
//}
