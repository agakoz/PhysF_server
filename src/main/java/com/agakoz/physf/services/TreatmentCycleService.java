package com.agakoz.physf.services;

import com.agakoz.physf.model.DTO.*;
import com.agakoz.physf.model.ExternalAttachment;
import com.agakoz.physf.model.TreatmentCycle;
import com.agakoz.physf.repositories.ExternalAttachmentRepository;
import com.agakoz.physf.repositories.TreatmentCycleRepository;
import com.agakoz.physf.repositories.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TreatmentCycleService {

    private final TreatmentCycleRepository treatmentCycleRepository;
    private final  PatientService patientService;
    private final VisitRepository visitRepository;
    private final  ExternalAttachmentRepository attachmentRepository;

    @Autowired
    public TreatmentCycleService(TreatmentCycleRepository treatmentCycleRepository, PatientService patientService, VisitRepository visitRepository, ExternalAttachmentRepository attachmentRepository) {
        this.treatmentCycleRepository = treatmentCycleRepository;
        this.patientService = patientService;
        this.visitRepository = visitRepository;
        this.attachmentRepository = attachmentRepository;
    }

    public List<TreatmentCycleTitleDTO> getStartedCyclesFromPatient(int patientId) {
        patientService.validatePatientIdForCurrentUser(patientId);
        List<TreatmentCycleTitleDTO> treatmentCycleTitles = treatmentCycleRepository.findStartedCyclesByPatientId(patientId);

        return treatmentCycleTitles;
    }

    public List<TreatmentCycleTitleBodyPartDTO> getTreatmentCycleTitlesAndBodyParts(int patientId) {
        patientService.validatePatientIdForCurrentUser(patientId);
        List<TreatmentCycleTitleBodyPartDTO> treatmentCycleTitles = treatmentCycleRepository.findAllTreatmentCycleTitleAndBodyPartByPatientId(patientId);

        return treatmentCycleTitles;
    }

    public void deleteTreatmentCycleIfHasNoVisits(TreatmentCycle treatmentCycle) {
        List<Integer> visitsConnected = treatmentCycleRepository.findFirstVisitConnectedToCycle(treatmentCycle.getId());
        if (visitsConnected.size() == 0) {
            treatmentCycleRepository.delete(treatmentCycle);
        }
    }

    public TreatmentCycleInfoDTO getTreatmentCycleInfo(int treatmentCycleId) {
        Optional<TreatmentCycleInfoDTO> treatmentCycleInfoDTO = treatmentCycleRepository.retrieveById(treatmentCycleId);
        if (treatmentCycleInfoDTO.isEmpty()) {
            throw new IllegalArgumentException();
        }
        else return treatmentCycleInfoDTO.get();
    }

    public List<VisitDateTimeInfo> getTreatmentCycleFinishedVisitTimeInfo(int treatmentCycleId) {
        List<VisitDateTimeInfo> visits = visitRepository.findFinishedVisitDateTimeInfoByTreatmentCycleId(treatmentCycleId);
        return visits;
    }

    public List<TreatmentCycleAttachmentDTO> getAttachmentsAssignedToTreatmentCycle(int treatmentCycleId) {
        List<TreatmentCycleAttachmentDTO> attachments = attachmentRepository.findAttachmentsAssignedToTreatmentCycle(treatmentCycleId);
        return attachments;
    }
}
