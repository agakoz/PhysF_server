package com.agakoz.physf.controllers;

import com.agakoz.physf.model.DTO.UserDTO;
import com.agakoz.physf.model.User;
import com.agakoz.physf.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserRepository userRepository;

    @Autowired
    public void UserController(UserRepository userRepository){

        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    public List<UserDTO> getAllUsers(){
        return userRepository.retrieveAllUserAsDTO();
    }

    @GetMapping("/{id}")
    public UserDTO getAllUsers(@PathVariable int id){
        return userRepository.retrieveUserAsDTOById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Integer id) {
        Optional<User> userToDelete = userRepository.findById(id);

        if (userToDelete.isPresent()) {
            userRepository.delete(userToDelete.get());
            return String.format("User with id= \"%d\" deleted successfully", id);

        } else {
            return String.valueOf(new IOException(String.format("User with id = \"%d\" does not exist!", id)));
        }
    }
}
