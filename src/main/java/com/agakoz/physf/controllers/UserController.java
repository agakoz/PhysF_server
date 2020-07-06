package com.agakoz.physf.controllers;

import com.agakoz.physf.model.DTO.UserDTO;
import com.agakoz.physf.model.User;
import com.agakoz.physf.repositories.UserRepository;
import com.agakoz.physf.security.AuthoritiesConstants;
import com.agakoz.physf.services.MailService;
import com.agakoz.physf.services.UserService;
import com.agakoz.physf.services.exceptions.UserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
public class UserController {
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    private String applicationName="";

    private UserRepository userRepository;
    private UserService userService;
    private final MailService mailService;

    @Autowired
    public UserController(UserRepository userRepository, UserService userService, MailService mailService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Object> getAllUsers() {
        try {
            List<UserDTO> userList = userService.getAllUsers();
            return new ResponseEntity<>(userList, HttpStatus.OK);

        } catch (UserException ex) {
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
        } catch (UserException ex) {
            return new ResponseEntity<>(
                    String.format("Error! %s", ex.getMessage()),
                    HttpStatus.NOT_FOUND);

        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteUser(@PathVariable Integer id) {
        try {
            userService.deleteUserById(id);
            return new ResponseEntity<>(
                    String.format("User with id= \"%d\" deleted successfully", id),
                    HttpStatus.OK);

        } catch (UserException ex) {
            return new ResponseEntity<>(
                    String.format("Error deleting! %s", ex.getMessage()),
                    HttpStatus.CONFLICT);
        }
    }


    @PutMapping("/{id}")
    ResponseEntity<String> updateUser(@PathVariable int id, User user) {
        try {
            userService.updateUser(id, user);
            return new ResponseEntity<>(
                    "user updated successfully",
                    HttpStatus.OK);
        } catch (UserException ex) {
            return new ResponseEntity<>(
                    "Update failed! " + ex.getMessage(),
                    HttpStatus.NOT_FOUND);
        }
    }
//

}
