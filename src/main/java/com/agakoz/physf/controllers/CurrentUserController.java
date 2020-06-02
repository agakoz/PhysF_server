package com.agakoz.physf.controllers;

import com.agakoz.physf.model.DTO.UserDTO;
import com.agakoz.physf.model.User;
import com.agakoz.physf.repositories.UserRepository;
import com.agakoz.physf.services.CurrentUserService;
import com.agakoz.physf.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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

            } catch (IOException ex) {
                return new ResponseEntity<>(
                        String.format("Error deleting! %s", ex.getMessage()),
                        HttpStatus.CONFLICT);
            }
        }



        @PutMapping("/update")
    ResponseEntity<String> updateUser(User user) {
            try {
                currentUserService.updateUser(user);
                return new ResponseEntity<>(
                        "user updated successfully",
                        HttpStatus.OK);
            } catch (IOException ex) {
                return new ResponseEntity<>(
                        "Update failed! " + ex.getMessage(),
                        HttpStatus.NOT_FOUND);
            }
        }

        @GetMapping("/")
    ResponseEntity<Object> getCurrentUser(){
            try {
                UserDTO user = currentUserService.getUser();
                return new ResponseEntity<>(user, HttpStatus.OK);
            } catch (IOException ex) {
                return new ResponseEntity<>(
                        String.format("Error! %s", ex.getMessage()),
                        HttpStatus.NOT_FOUND);

            }
        }
    }
