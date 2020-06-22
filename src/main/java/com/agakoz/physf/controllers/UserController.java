package com.agakoz.physf.controllers;

import com.agakoz.physf.model.DTO.UserDTO;
import com.agakoz.physf.model.User;
import com.agakoz.physf.repositories.UserRepository;
import com.agakoz.physf.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
public class UserController {
    private UserRepository userRepository;
    private UserService userService;

    @Autowired
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllUsers() throws IOException {
        try {
            List<UserDTO> userList = userService.getAllUsers();
            return new ResponseEntity<>(userList, HttpStatus.OK);

        } catch (IOException ex) {
            return new ResponseEntity<>(
                    String.format("Error! %s", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable int id) {
        try {
            UserDTO user = userService.getUserById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (IOException ex) {
            return new ResponseEntity<>(
                    String.format("Error! %s", ex.getMessage()),
                    HttpStatus.NOT_FOUND);

        }
    }

        @DeleteMapping("/{id}")
        ResponseEntity<String> deleteUser (@PathVariable Integer id){
            try {
                userService.deleteUser(id);
                return new ResponseEntity<>(
                        String.format("User with id= \"%d\" deleted successfully", id),
                        HttpStatus.OK);

            } catch (IOException ex) {
                return new ResponseEntity<>(
                        String.format("Error deleting! %s", ex.getMessage()),
                        HttpStatus.CONFLICT);
            }
        }



        @PutMapping("/{id}")
    ResponseEntity<String> updateUser(@PathVariable int id,  User user) {
            try {
                userService.updateUser(id, user);
                return new ResponseEntity<>(
                        "user updated successfully",
                        HttpStatus.OK);
            } catch (IOException ex) {
                return new ResponseEntity<>(
                        "Update failed! " + ex.getMessage(),
                        HttpStatus.NOT_FOUND);
            }
        }
    }
