package com.agakoz.physf.controllers;

import com.agakoz.physf.model.DTO.TreatmentCycleInfoDTO;
import com.agakoz.physf.model.DTO.TreatmentCycleTitleBodyPartDTO;
import com.agakoz.physf.model.DTO.TreatmentCycleTitleDTO;
import com.agakoz.physf.model.DTO.VisitDateTimeInfo;
import com.agakoz.physf.services.TreatmentCycleService;
import com.agakoz.physf.services.VisitService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/treatmentCycle")
public class TreatmentCycleController {

    private VisitService visitService;
    private TreatmentCycleService treatmentCycleService;

    @Autowired
    public TreatmentCycleController(VisitService visitService, TreatmentCycleService treatmentCycleService) {
        this.visitService = visitService;
        this.treatmentCycleService = treatmentCycleService;
    }

    @GetMapping("/getAll/{patientId}")
    @SneakyThrows
    public ResponseEntity<Object> getAllFromPatient(@PathVariable int patientId) {
        List<TreatmentCycleTitleDTO> treatmentCycleTitles = treatmentCycleService.getStartedCyclesFromPatient(patientId);
        return new ResponseEntity<>(treatmentCycleTitles, HttpStatus.OK);
    }

    @GetMapping("/getTreatmentCycleTitlesAndBodyParts/{patientId}")
    @SneakyThrows
    @ResponseBody
    public List<TreatmentCycleTitleBodyPartDTO> getTreatmentCycleTitlesAndBodyParts(@PathVariable int patientId) {
        List<TreatmentCycleTitleBodyPartDTO> treatmentCycles = treatmentCycleService.getTreatmentCycleTitlesAndBodyParts(patientId);
        return treatmentCycles;
    }

    @GetMapping("/getCycleInfo/{treatmentCycleId}")
    @ResponseBody
    @SneakyThrows
    public TreatmentCycleInfoDTO getTreatmentCycleInfo(@PathVariable int treatmentCycleId) {
        TreatmentCycleInfoDTO treatmentCycleInfo = treatmentCycleService.getTreatmentCycleInfo(treatmentCycleId);
        return treatmentCycleInfo;
    }


    @GetMapping("/getTreatmentCycleVisitsTimeInfo/{treatmentCycleId}")
    @ResponseBody
    @SneakyThrows
    public List<VisitDateTimeInfo> getTreatmentCycleVisitTimeInfo(@PathVariable int treatmentCycleId) {
        List<VisitDateTimeInfo> visits = treatmentCycleService.getTreatmentCycleFinishedVisitTimeInfo(treatmentCycleId);
        return visits;
    }

}
