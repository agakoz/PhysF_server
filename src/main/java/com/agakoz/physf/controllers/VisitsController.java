package com.agakoz.physf.controllers;

import com.agakoz.physf.model.DTO.*;
import com.agakoz.physf.services.VisitService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/visit")
public class VisitsController {
    private VisitService visitService;

    @Autowired
    public VisitsController(VisitService visitService) {
        this.visitService = visitService;
    }

    @PostMapping("/planFirstVisit/{patientId}")
    @SneakyThrows
    public ResponseEntity<Object> planFirstVisitInNewCycle(@PathVariable int patientId, @RequestBody FirstVisitPlanDTO visitPlan) {
        int visitId = visitService.planFirstVisit(visitPlan, patientId);
        return new ResponseEntity<>(visitId, HttpStatus.OK);
    }

    @PostMapping("/planNextVisit")
    @SneakyThrows
    public ResponseEntity<Object> planNextVisit(@RequestBody VisitPlanWithTreatmentCycleDTO visitPlan) {
        int visitId = visitService.planNextVisit(visitPlan);
        return new ResponseEntity<>(visitId, HttpStatus.OK);
    }

    @PostMapping("cancel/{visitId}")
    @SneakyThrows
    public ResponseEntity<Object> cancelVisit(@PathVariable int visitId) {
        visitService.cancelVisit(visitId);
        return new ResponseEntity<>(
                String.format("Visit canceled successfully"),
                HttpStatus.OK);
    }

    @PostMapping("updateVisitPlan/{visitId}")
    @SneakyThrows
    public ResponseEntity<Object> updateVisitPlan(@PathVariable int visitId, @RequestBody VisitPlanWithTreatmentCycleDTO updatedVisitPlan) {
        visitService.updateVisitPlan(visitId, updatedVisitPlan);
        return new ResponseEntity<>(
                String.format("Visit canceled successfully"),
                HttpStatus.OK);
    }

//
//    @GetMapping("/all")
//    public ResponseEntity<Object> getAllVisits() {
//        try {
//            List<VisitWithPhotosDTO> visits = visitService.getAllVisits();
//            return new ResponseEntity<>(visits, HttpStatus.OK);
//
//        } catch (NoVisitsException ex) {
//            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
//
//        }
//    }
//
//    @GetMapping("/{visitId}")
//    public ResponseEntity<Object> getVisitById(@PathVariable int visitId) {
//        try {
//            VisitWithPhotosDTO visit = visitService.getById(visitId);
//            return new ResponseEntity<>(visit, HttpStatus.OK);
//
//        } catch (VisitNotExistsException ex) {
//            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
//
//        }
//    }
//
//    @GetMapping("/patient/{patientId}")
//    public ResponseEntity<Object> getPatientVisits(@PathVariable int patientId) {
//        try {
//            List<VisitWithPhotosDTO> visits = visitService.getPatientVisits(patientId);
//            return new ResponseEntity<>(visits, HttpStatus.OK);
//
//        } catch (NoVisitsException | PatientWithIdNotExistsException ex) {
//            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
//    public ResponseEntity<String> addNewVisit(VisitCreateUpdateDTO visit, @RequestParam(value = "photos", required = false) MultipartFile[] photos) {
//        try {
//            visitService.addNewVisit(visit, photos);
//            return new ResponseEntity<>("visits created successfully", HttpStatus.CREATED);
//
//        } catch (PatientWithIdNotExistsException ex) {
//            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
//
//        } catch (IOException ex) {
//            return new ResponseEntity<>(String.format("Error! %s"), HttpStatus.NOT_FOUND);
//
//        }
//    }
//
//
//
//    @PutMapping(value = "/{visitId}/update", consumes = "multipart/form-data")
//    public ResponseEntity<String> updateVisit(@PathVariable int visitId, VisitCreateUpdateDTO visit, @RequestParam(value = "photos", required = false) MultipartFile[] photos) {
//        try {
//            visitService.update(visitId, visit, photos);
//            return new ResponseEntity<>("visits updated successfully", HttpStatus.OK);
//
//        } catch (PatientWithIdNotExistsException | VisitNotExistsException ex) {
//            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
//
//        } catch (IOException ex) {
//            return new ResponseEntity<>(String.format("Error! File Problem. ", ex.getMessage()), HttpStatus.NOT_FOUND);
//
//        }
//    }
//
//    @DeleteMapping("/{visitId}/delete")
//    public ResponseEntity<String> deleteVisit(@PathVariable int visitId) {
//        try {
//            visitService.delete(visitId);
//            return new ResponseEntity<>("visits deleted successfully", HttpStatus.OK);
//
//        } catch (VisitNotExistsException ex) {
//            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
//
//        }
//    }
//
////planned visits
//@GetMapping("/planned/all")
//public ResponseEntity<Object> getPlannedVisits() {
//    try {
//        List<VisitPlanDTO> visits = visitService.getAllPlannedVisits();
//        return new ResponseEntity<>(visits, HttpStatus.OK);
//    } catch (NoVisitsException ex) {
//        return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
//    }
//}
//
//
//    @PostMapping("/planned/create")
//    public ResponseEntity<String> planVisit(VisitPlanCreateUpdateDTO visitPlanCreateUpdateDTO) {
//        try {
//            visitService.planVisit(visitPlanCreateUpdateDTO);
//            return new ResponseEntity<>("visit planned successfully", HttpStatus.CREATED);
//
//        } catch (IllegalArgumentException ex) {
//            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
//
//        }
//    }
//
//    @PutMapping("/planned/edit/{visitId}")
//    public ResponseEntity<String> editPlannedVisit(@PathVariable int visitId, VisitPlanCreateUpdateDTO visitPlanCreateUpdateDTO) {
//        try {
//            visitService.editPlannedVisit(visitId, visitPlanCreateUpdateDTO);
//            return new ResponseEntity<>("visit plan updated successfully", HttpStatus.OK);
//
//        } catch (IllegalArgumentException ex) {
//            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
//
//        }
//    }
//    @GetMapping("/planned/{date}")
//    public ResponseEntity<Object> getPlannedVisitsByDate(Date date) {
//        try {
//            List<VisitPlanDTO> visits = visitService.getPlannedVisitsByDate(date);
//            return new ResponseEntity<>(visits, HttpStatus.OK);
//        } catch (Exception ex) {
//            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
//        }
//    }
//

}
