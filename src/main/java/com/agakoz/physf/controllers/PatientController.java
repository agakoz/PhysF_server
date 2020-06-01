package com.agakoz.physf.controllers;

import com.agakoz.physf.model.DTO.PatientDTO;
import com.agakoz.physf.model.User;
import com.agakoz.physf.repositories.PatientRepository;
import com.agakoz.physf.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {
    PatientRepository patientRepository;
    PatientService patientService;

    @Autowired
    public PatientController(PatientRepository patientRepository, PatientService patientService) {
        this.patientRepository = patientRepository;
        this.patientService = patientService;
    }

    @GetMapping("/")
    public ResponseEntity<Object> getAllPatientFromCurrentUser(){
        try {
            List<PatientDTO> patients = patientService.getAllPatientsFromCurrentUser();
            return new ResponseEntity<>(patients, HttpStatus.OK);
        }catch(IOException ex){
            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
//    @GetMapping("/all/{userId}")
//    public List<PatientDTO> getAllPatientFromCurrentUser(@PathVariable int userId) {
//
//        List<PatientDTO> patients = patientRepository.retrievePatientsDTOByUserId(userId);
//        return patients;
//
//    }
@GetMapping("/{id}")
public ResponseEntity<Object> getPatientByIdFromCurrentUser(@PathVariable int id){
    try {
        PatientDTO patient = patientService.getPatientByIdFromCurrentUser(id);
        return new ResponseEntity<>(patient, HttpStatus.OK);
    }catch(IOException ex){
        return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
}
