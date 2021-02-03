package com.agakoz.physf.services;

import com.agakoz.physf.model.DTO.TreatmentCycleInfoDTO;
import com.agakoz.physf.model.DTO.TreatmentCycleTitleBodyPartDTO;
import com.agakoz.physf.model.DTO.TreatmentCycleTitleDTO;
import com.agakoz.physf.model.DTO.VisitDateTimeInfo;
import com.agakoz.physf.model.TreatmentCycle;
import com.agakoz.physf.repositories.TreatmentCycleRepository;
import com.agakoz.physf.repositories.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TreatmentCycleService {

    TreatmentCycleRepository treatmentCycleRepository;
    PatientService patientService;
    VisitRepository visitRepository;

    @Autowired
    public TreatmentCycleService(TreatmentCycleRepository treatmentCycleRepository, PatientService patientService, VisitRepository visitRepository) {
        this.treatmentCycleRepository = treatmentCycleRepository;
        this.patientService = patientService;
        this.visitRepository = visitRepository;
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
}
