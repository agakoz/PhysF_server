package com.agakoz.physf.services;

import com.agakoz.physf.model.DTO.TreatmentCycleTitleDTO;
import com.agakoz.physf.model.TreatmentCycle;
import com.agakoz.physf.repositories.TreatmentCycleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreatmentCycleService {

    TreatmentCycleRepository treatmentCycleRepository;
    PatientService patientService;

    @Autowired
    public TreatmentCycleService(TreatmentCycleRepository treatmentCycleRepository, PatientService patientService) {
        this.treatmentCycleRepository = treatmentCycleRepository;
        this.patientService = patientService;
    }

    public List<TreatmentCycleTitleDTO> getAllCyclesFromPatient(int patientId) {
        patientService.validatePatientIdForCurrentUser(patientId);
        List<TreatmentCycleTitleDTO> treatmentCycleTitles = treatmentCycleRepository.findAllByPatientId(patientId);

        return treatmentCycleTitles;
    }

    public void deleteTreatmentCycleIfHasNoVisits(TreatmentCycle treatmentCycle) {
        List<Integer> visitsConnected = treatmentCycleRepository.findFirstVisitConnectedToCycle(treatmentCycle.getId());
        if (visitsConnected.size() == 0) {
            treatmentCycleRepository.delete(treatmentCycle);
        }
    }
}
