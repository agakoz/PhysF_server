package com.agakoz.physf.controllers;

import com.agakoz.physf.model.DTO.VisitCreateUpdateDTO;
import com.agakoz.physf.model.DTO.VisitWithPhotosDTO;
import com.agakoz.physf.services.VisitService;
import com.agakoz.physf.services.exceptions.NoVisitsException;
import com.agakoz.physf.services.exceptions.PatientWithIdNotExistsException;
import com.agakoz.physf.services.exceptions.VisitNotExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
            List<VisitWithPhotosDTO> visits = visitService.getAllVisits();
            return new ResponseEntity<>(visits, HttpStatus.OK);

        } catch (NoVisitsException ex) {
            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);

        }
    }

    @GetMapping("/{visitId}")
    public ResponseEntity<Object> getVisitById(@PathVariable int visitId) {
        try {
            VisitWithPhotosDTO visit = visitService.getById(visitId);
            return new ResponseEntity<>(visit, HttpStatus.OK);

        } catch (VisitNotExistsException ex) {
            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);

        }
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Object> getPatientVisits(@PathVariable int patientId) {
        try {
            List<VisitWithPhotosDTO> visits = visitService.getPatientVisits(patientId);
            return new ResponseEntity<>(visits, HttpStatus.OK);

        } catch (NoVisitsException | PatientWithIdNotExistsException ex) {
            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    public ResponseEntity<String> addVisit(VisitCreateUpdateDTO visit, @RequestParam(value = "photos", required = false) MultipartFile[] photos) {
        try {
            visitService.addVisit(visit, photos);
            return new ResponseEntity<>("visits created successfully", HttpStatus.CREATED);

        } catch (PatientWithIdNotExistsException ex) {
            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);

        } catch (IOException ex) {
            return new ResponseEntity<>(String.format("Error! %s"), HttpStatus.NOT_FOUND);

        }
    }

//    @Bean(name = "multipartResolver")
//    public CommonsMultipartResolver commonsMultipartResolver(){
//        return new CommonsMultipartResolver();
//    }

    @PutMapping(value = "/{visitId}/update", consumes = "multipart/form-data")
    public ResponseEntity<String> updateVisit(@PathVariable int visitId, VisitCreateUpdateDTO visit, @RequestParam(value = "photos", required = false) MultipartFile[] photos) {
        try {
            visitService.update(visitId, visit, photos);
            return new ResponseEntity<>("visits updated successfully", HttpStatus.OK);

        } catch (PatientWithIdNotExistsException | VisitNotExistsException ex) {
            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);

        } catch (IOException ex) {
            return new ResponseEntity<>(String.format("Error! File Problem. ", ex.getMessage()), HttpStatus.NOT_FOUND);

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

//    @GetMapping("/{visitId}/photos")
//    public ResponseEntity<Object> getPhotos(@PathVariable int visitId) {
//        try {
//            List<PhotoDTO> photos = visitService.getPhotosFromVisit(visitId);
//            return new ResponseEntity<>(photos, HttpStatus.OK);
//
//        } catch (NoPhotosException ex) {
//            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
//
//        }
//    }

}
