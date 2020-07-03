//package com.agakoz.physf.controllers;
//
//import com.agakoz.physf.model.DTO.VisitPlanCreateUpdateDTO;
//import com.agakoz.physf.model.DTO.VisitPlanDTO;
//import com.agakoz.physf.services.PlannedVisitService;
//import com.agakoz.physf.services.VisitService;
//import com.agakoz.physf.services.exceptions.NoVisitsException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.sql.Date;
//import java.util.List;
//
//@RestController
//@RequestMapping("/calendar")
//public class PlannedVisitsController {
//
//    private PlannedVisitService plannedVisitService;
//
//    @Autowired
//    public PlannedVisitsController(PlannedVisitService plannedVisitService) {
//        this.plannedVisitService = plannedVisitService;
//    }
//
//    @GetMapping("/planned/all")
//    public ResponseEntity<Object> getPlannedVisits() {
//        try {
//            List<VisitPlanDTO> visits = plannedVisitService.getAllPlannedVisits();
//            return new ResponseEntity<>(visits, HttpStatus.OK);
//        } catch (NoVisitsException ex) {
//            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
//        }
//    }
//    @GetMapping("/planned/{date}")
//    public ResponseEntity<Object> getPlannedVisitsByDate(Date date) {
//        try {
//            List<VisitPlanDTO> visits = VisitService.getPlannedVisitsByDate(date);
//            return new ResponseEntity<>(visits, HttpStatus.OK);
//        } catch (NoVisitsException ex) {
//            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @PostMapping("/planned/create")
//    public ResponseEntity<String> planVisit(VisitPlanCreateUpdateDTO visitPlanCreateUpdateDTO) {
//        try {
//            plannedVisitService.planVisit(visitPlanCreateUpdateDTO);
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
//            plannedVisitService.editPlannedVisit(visitId, visitPlanCreateUpdateDTO);
//            return new ResponseEntity<>("visit plan updated successfully", HttpStatus.OK);
//
//        } catch (IllegalArgumentException ex) {
//            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
//
//        }
//    }
//
////    @DeleteMapping("delete/{plannedVisitId}")
////    public ResponseEntity<String> deletePlannedVisit(@PathVariable int plannedVisitId) {
////        try {
////            plannedVisitService.delete(plannedVisitId);
////            return new ResponseEntity<>("visit plan updated successfully", HttpStatus.OK);
////
////        } catch (IllegalArgumentException ex) {
////            return new ResponseEntity<>(String.format("Error! %s", ex.getMessage()), HttpStatus.NOT_FOUND);
////
////        }
////    }
//}
