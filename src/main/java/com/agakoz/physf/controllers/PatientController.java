package com.agakoz.physf.controllers;

import com.agakoz.physf.model.DTO.IncomingVisitDTO;
import com.agakoz.physf.model.DTO.PatientCreateOrUpdateDTO;
import com.agakoz.physf.model.DTO.PatientDTO;
import com.agakoz.physf.model.DTO.FirstVisitPlanDTO;
import com.agakoz.physf.services.PatientService;
import com.agakoz.physf.services.VisitService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/patient")
public class PatientController {
    private final PatientService patientService;
    private final VisitService visitService;

    @Autowired
    public PatientController(PatientService patientService, VisitService visitService) {
        this.patientService = patientService;
        this.visitService = visitService;
    }

    @GetMapping("/all")
    @SneakyThrows
    public ResponseEntity<Object> getAllPatientFromCurrentUser() {

        List<PatientDTO> patients = patientService.getAllPatientsFromCurrentUser();
        return new ResponseEntity<>(patients, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getPatientByIdFromCurrentUser(@PathVariable int id) {
        try {
            PatientDTO patient = patientService.getPatientByIdFromCurrentUser(id);
            return new ResponseEntity<>(patient, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add")
    @SneakyThrows
    @Transactional
    public ResponseEntity<Object> addPatient(@RequestBody PatientCreateOrUpdateDTO patient) {
        System.out.println("adding patient controller");
        patientService.addPatient(patient);
        return new ResponseEntity<>(patient, HttpStatus.CREATED);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePatientById(@PathVariable int id) {
        try {

            patientService.deletePatient(id);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);

        }
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deletePatientById(@RequestBody List<Integer> ids) {
        try {
            System.out.println(ids);
            patientService.deletePatients(ids);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);

        }
    }

    @DeleteMapping("/delete/all")
    @SneakyThrows
    public ResponseEntity<String> deleteAllPatientsFromCurrentUser() {

        patientService.deleteAllPatientsFromCurrentUser();
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);

    }

    @PostMapping("/update/{id}")
    @SneakyThrows
    ResponseEntity<PatientCreateOrUpdateDTO> updatePatient(@PathVariable int id, @RequestBody PatientCreateOrUpdateDTO patient) {
//        try {
        PatientCreateOrUpdateDTO updatedPatient = patientService.updatePatient(id, patient);
        return ResponseEntity.ok(updatedPatient);
//        } catch (Exception ex) {
//            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
//        }
    }

    @GetMapping("/{patientId}/incomingVisits")
    @SneakyThrows
    public ResponseEntity<Object> getAllPatientFromCurrentUser(@PathVariable int patientId) {

        List<IncomingVisitDTO> incomingVisits = visitService.getIncomingVisits(patientId);
        return new ResponseEntity<>(incomingVisits, HttpStatus.OK);
    }

}
