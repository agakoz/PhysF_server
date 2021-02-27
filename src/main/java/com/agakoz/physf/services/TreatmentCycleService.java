package com.agakoz.physf.services;

import com.agakoz.physf.model.DTO.*;
import com.agakoz.physf.model.ExternalAttachment;
import com.agakoz.physf.model.TreatmentCycle;
import com.agakoz.physf.repositories.TreatmentCycleRepository;
import com.agakoz.physf.repositories.VisitRepository;
import com.agakoz.physf.services.exceptions.TreatmentCycleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TreatmentCycleService {

    private final TreatmentCycleRepository treatmentCycleRepository;
    private final PatientService patientService;
    private final VisitRepository visitRepository;
    private final ExternalAttachmentService externalAttachmentService;

    @Autowired
    public TreatmentCycleService(TreatmentCycleRepository treatmentCycleRepository, PatientService patientService, VisitRepository visitRepository, ExternalAttachmentService externalAttachmentService) {
        this.treatmentCycleRepository = treatmentCycleRepository;
        this.patientService = patientService;
        this.visitRepository = visitRepository;
        this.externalAttachmentService = externalAttachmentService;
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
        } else return treatmentCycleInfoDTO.get();
    }

    public List<VisitDateTimeInfo> getTreatmentCycleFinishedVisitTimeInfo(int treatmentCycleId) {
        List<VisitDateTimeInfo> visits = visitRepository.findFinishedVisitDateTimeInfoByTreatmentCycleId(treatmentCycleId);
        return visits;
    }

    public List<ExternalAttachmentDTO> getAttachmentsAssignedToTreatmentCycle(int treatmentCycleId) {
        List<ExternalAttachmentDTO> attachments = externalAttachmentService.getAttachmentsAssignedToTreatmentCycle(treatmentCycleId);
        return attachments;
    }

    public void updateExternalAttachmentsForTreatmentCycle(int treatmentCycleId, ExternalAttachmentsWrapper attachments) throws TreatmentCycleException, NoSuchFileException {

        List<Integer> updatedAttachmentIds = attachments.getAttachments()
                .stream().map(ExternalAttachmentDTO::getId)
                .filter(id -> id > -1)
                .collect(Collectors.toList());
        externalAttachmentService.removeOldAssignedAttachments(treatmentCycleId, updatedAttachmentIds);

        attachments.getAttachments().forEach(attachment -> {
            try {
                externalAttachmentService.createOrUpdateAttachment(attachment, treatmentCycleId);
            } catch (NoSuchFileException e) {
                e.printStackTrace();
            }
        });
    }

    private boolean deleteExternalAttachment(int attachmentId) {
        Optional<ExternalAttachment> attachmentToDeleteOpt = externalAttachmentService.getOptionalAttachment(attachmentId);
        if (attachmentToDeleteOpt.isEmpty()) {
            return false;
        }
        ExternalAttachment attachmentToDelete = attachmentToDeleteOpt.get();

        boolean isRemoved = externalAttachmentService.deleteExternalAttachmentWithFile(attachmentToDelete);
        return isRemoved;
    }
}
