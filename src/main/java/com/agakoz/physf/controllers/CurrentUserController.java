package com.agakoz.physf.controllers;

import com.agakoz.physf.model.DTO.CurrentUserAccountDTO;
import com.agakoz.physf.model.DTO.CurrentUserDTO;
import com.agakoz.physf.model.DTO.CurrentUserPersonalDTO;
import com.agakoz.physf.repositories.UserRepository;
import com.agakoz.physf.services.CurrentUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/profile")
public class CurrentUserController {
    private UserRepository userRepository;
    private CurrentUserService currentUserService;

    @Autowired
    public CurrentUserController(UserRepository userRepository, CurrentUserService currentUserService) {
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }


        @DeleteMapping("/{id}")
        ResponseEntity<String> deleteUser (@PathVariable Integer id){
            try {
                currentUserService.deleteUser();
                return new ResponseEntity<>(
                        String.format("User with id= \"%d\" deleted successfully", id),
                        HttpStatus.OK);

            } catch (Exception ex) {
                return new ResponseEntity<>(
                        String.format("Error deleting! %s", ex.getMessage()),
                        HttpStatus.CONFLICT);
            }
        }



        @PutMapping("/update/account")
    ResponseEntity<String> updateUser(CurrentUserAccountDTO userAccountDTO) {
            try {
                currentUserService.updateCurrentUserAccount(userAccountDTO);
                return new ResponseEntity<>(
                        "user account info updated successfully",
                        HttpStatus.OK);
            } catch (Exception ex) {
                return new ResponseEntity<>(
                        "Update failed! " + ex.getMessage(),
                        HttpStatus.NOT_FOUND);
            }
        }
    @PutMapping("/update/personal")
    ResponseEntity<String> updateUser(CurrentUserPersonalDTO userPersonalDTO) {
        try {
            currentUserService.updateCurrentUserPersonal(userPersonalDTO);
            return new ResponseEntity<>(
                    "user personal info updated successfully",
                    HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>(
                    "Update failed! " + ex.getMessage(),
                    HttpStatus.NOT_FOUND);
        }
    }

        @GetMapping("/info")
    ResponseEntity<Object> getCurrentUser(){
            try {
                CurrentUserDTO user = currentUserService.getCurrentUserDTO();
                return new ResponseEntity<>(user, HttpStatus.OK);
            } catch (Exception ex) {
                return new ResponseEntity<>(
                        String.format("Error! %s", ex.getMessage()),
                        HttpStatus.NOT_FOUND);

            }
        }
    }
