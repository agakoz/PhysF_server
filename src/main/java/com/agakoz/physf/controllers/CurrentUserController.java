package com.agakoz.physf.controllers;

import com.agakoz.physf.model.DTO.*;
import com.agakoz.physf.model.User;
import com.agakoz.physf.repositories.UserRepository;
import com.agakoz.physf.services.CurrentUserService;
import com.agakoz.physf.services.MailService;
import com.agakoz.physf.services.exceptions.CurrentUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping("/profile")
public class CurrentUserController {
    private UserRepository userRepository;
    private CurrentUserService currentUserService;
    private MailService mailService;

    @Autowired
    public CurrentUserController(UserRepository userRepository, CurrentUserService currentUserService, MailService mailService) {
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
        this.mailService = mailService;
    }


        @DeleteMapping("/delete")
        ResponseEntity<String> deleteUser (){
            try {
                currentUserService.deleteUser();
                return new ResponseEntity<>(
                        String.format("User deleted successfully"),
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



    @PostMapping(path = "/change-password")
    public void changePassword( PasswordChangeDTO passwordChangeDto) {
//        if (!checkPasswordLength(passwordChangeDto.getNewPassword())) {
//            throw new InvalidPasswordException();
//        }
        currentUserService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid UserCreateDTO userCreateDTO) {

        User user = currentUserService.registerUser(userCreateDTO);
        mailService.sendActivationEmail(user);
    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GetMapping("/activate")
    public ResponseEntity<String>  activateAccount(@RequestParam(value = "key") String key) {
        try{
            Optional<User> user = currentUserService.activateRegistration(key);
            if (!user.isPresent()) {
                throw new CurrentUserException("No user was found for this activation key");
            }
            return new ResponseEntity<>("the account has been activated", HttpStatus.OK);
        }catch (CurrentUserException ex){
            return new ResponseEntity<>(
                    String.format("Error! %s", ex.getMessage()),
                    HttpStatus.NOT_FOUND);
        }

    }



}
