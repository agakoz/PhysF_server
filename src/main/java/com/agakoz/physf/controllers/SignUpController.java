package com.agakoz.physf.controllers;

import com.agakoz.physf.model.User;
import com.agakoz.physf.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignUpController {
    private UserService userService;

    @Autowired
    public SignUpController(UserService userService){
        this.userService= userService;
    }


    @GetMapping("/sign-up")
    public String singUp(Model model) {
        model.addAttribute("user", new User());
        return "sign-up";
    }

    @PostMapping("/register")
    ResponseEntity<String> register (User user){
        try {
            userService.createAndAddUser(user);
            return new ResponseEntity<>(
                    "user registered successfully",
                    HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(
                    "Registration failed " + ex.getMessage(),
                    HttpStatus.NOT_FOUND);
        }
    }
}
