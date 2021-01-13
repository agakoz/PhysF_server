package com.agakoz.physf.controllers;

import com.agakoz.physf.model.DTO.TreatmentCycleTitleDTO;
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
        List<TreatmentCycleTitleDTO> treatmentCycleTitles = treatmentCycleService.getAllCyclesFromPatient(patientId);
        return new ResponseEntity<>(treatmentCycleTitles, HttpStatus.OK);
    }
}
