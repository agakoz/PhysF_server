package com.agakoz.physf.controllers;

import com.agakoz.physf.model.DTO.PatientDTO;
import com.agakoz.physf.model.DTO.PlannedVisitCreateDTO;
import com.agakoz.physf.model.DTO.PlannedVisitDTO;
import com.agakoz.physf.services.PlannedVisitService;
import com.agakoz.physf.services.exceptions.NoVisitsException;
import com.agakoz.physf.services.exceptions.PatientWithIdNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/plannedVisits")
public class PlannedVisitsController {

    private PlannedVisitService plannedVisitService;

    @Autowired
    public PlannedVisitsController(PlannedVisitService plannedVisitService) {
        this.plannedVisitService = plannedVisitService;
    }

    @GetMapping("/")
    public ResponseEntity<Object> getPlannedVisits(){
        try {
            List<PlannedVisitDTO> visits = plannedVisitService.getAllPlannedVisits();
            return new ResponseEntity<>(visits, HttpStatus.OK);
        } catch (NoVisitsException ex) {
            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addPlannedVisit(PlannedVisitCreateDTO plannedVisitCreateDTO){
        try {
            plannedVisitService.planVisit(plannedVisitCreateDTO);
            return new ResponseEntity<>("visit planned successfully", HttpStatus.CREATED);

        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);

        }
    }
}
