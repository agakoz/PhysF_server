package com.agakoz.physf.controllers;

import com.agakoz.physf.model.DTO.PatientDTO;
import com.agakoz.physf.model.Patient;
import com.agakoz.physf.model.User;
import com.agakoz.physf.repositories.PatientRepository;
import com.agakoz.physf.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Object> getAllPatientFromCurrentUser() {
        try {
            List<PatientDTO> patients = patientService.getAllPatientsFromCurrentUser();
            return new ResponseEntity<>(patients, HttpStatus.OK);
        } catch (IOException ex) {
            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getPatientByIdFromCurrentUser(@PathVariable int id) {
        try {
            PatientDTO patient = patientService.getPatientByIdFromCurrentUser(id);
            return new ResponseEntity<>(patient, HttpStatus.OK);
        } catch (IOException ex) {
            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/add")
    public ResponseEntity<Object> addPatient(Patient patient){
        try {
            patientService.addPatient(patient);
            return new ResponseEntity<>(patient, HttpStatus.CREATED);
        } catch (IOException ex) {
            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePatientById(@PathVariable int id){
        try{
            patientService.deletePatient(id);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (IOException ex){
            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);

        }
    }
    @DeleteMapping("/delete/all")
    public ResponseEntity<String> deleteAllPatientsFromCurrentUser(){
        try{
            patientService.deleteAllPatientsFromCurrentUser();
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (IOException ex){
            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);

        }
    }
    @PutMapping("/{id}")
    ResponseEntity<String> updatePatient(@PathVariable int id,  Patient patient) {
        try {
            patientService.updatePatient(id, patient);
            return new ResponseEntity<>(
                    "patient updated successfully",
                    HttpStatus.OK);
        } catch (IOException ex) {
            return new ResponseEntity<>(
                    "Update failed! " + ex.getMessage(),
                    HttpStatus.NOT_FOUND);
        }
    }

}
