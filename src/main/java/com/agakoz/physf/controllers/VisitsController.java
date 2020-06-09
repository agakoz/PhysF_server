package com.agakoz.physf.controllers;

import com.agakoz.physf.model.DTO.VisitCreateUpdateDTO;
import com.agakoz.physf.model.DTO.VisitDTO;
import com.agakoz.physf.services.VisitService;
import com.agakoz.physf.services.exceptions.NoVisitsException;
import com.agakoz.physf.services.exceptions.PatientWithIdNotExistsException;
import com.agakoz.physf.services.exceptions.VisitNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile/visits")
public class VisitsController {
    private VisitService visitService;

    @Autowired
    public VisitsController(VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllVisits() {
        try {
            List<VisitDTO> visits = visitService.getAllVisits();
            return new ResponseEntity<>(visits, HttpStatus.OK);

        } catch (NoVisitsException ex) {
            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);

        }
    }
    @GetMapping("/{visitId}")
    public ResponseEntity<Object> getVisitById(@PathVariable int visitId) {
        try {
            VisitDTO visit = visitService.getById(visitId);
            return new ResponseEntity<>(visit, HttpStatus.OK);

        } catch (VisitNotExistsException ex) {
            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);

        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Object> getPatientVisits(@PathVariable int patientId) {
        try {
            List<VisitDTO> visits = visitService.getPatientVisits(patientId);
            return new ResponseEntity<>(visits, HttpStatus.OK);

        } catch (NoVisitsException | PatientWithIdNotExistsException ex) {
            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> addVisit(VisitCreateUpdateDTO visit) {
        try {
            visitService.addVisit(visit);
            return new ResponseEntity<>("visits created successfully", HttpStatus.CREATED);

        } catch (PatientWithIdNotExistsException ex) {
            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);

        }
    }

    @PutMapping("/{visitId}/update")
    public ResponseEntity<String> updateVisit(@PathVariable int visitId, VisitCreateUpdateDTO visit) {
        try {
            visitService.update(visitId, visit);
            return new ResponseEntity<>("visits updated successfully", HttpStatus.OK);

        } catch (PatientWithIdNotExistsException ex) {
            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);

        } catch (VisitNotExistsException ex) {
            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);

        }
    }

    @DeleteMapping("/{visitId}/delete")
    public ResponseEntity<String> deleteVisit(@PathVariable int visitId) {
        try {
            visitService.delete(visitId);
            return new ResponseEntity<>("visits deleted successfully", HttpStatus.OK);

        } catch (VisitNotExistsException ex) {
            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);

        }
    }


}
