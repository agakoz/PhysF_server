package com.agakoz.physf.controllers;

import com.agakoz.physf.model.DTO.*;
import com.agakoz.physf.repositories.UserRepository;
import com.agakoz.physf.services.UserService;
import com.agakoz.physf.services.MailService;
import com.agakoz.physf.services.exceptions.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/profile")
public class CurrentUserController {
    private UserRepository userRepository;
    private UserService userService;
    private MailService mailService;

    @Autowired
    public CurrentUserController(UserRepository userRepository, UserService userService, MailService mailService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
    }


        @DeleteMapping("/delete")
        ResponseEntity<String> deleteUser (){
            try {
                userService.deleteCurrentUser();
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
    ResponseEntity<String> updateUser(LoginRequest userAccountDTO) {
            try {
                userService.updateCurrentUserAccount(userAccountDTO);
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
            userService.updateCurrentUserPersonal(userPersonalDTO);
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
                CurrentUserDTO user = userService.getCurrentUserDTO();
                return new ResponseEntity<>(user, HttpStatus.OK);
            } catch (UserException ex) {
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
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }


//    @PostMapping("/register")
//    @ResponseStatus(HttpStatus.CREATED)
//    public void registerAccount(@Valid UserCreateDTO userCreateDTO) {
//
//        User user = userService.registerUser(userCreateDTO);
//        mailService.sendActivationEmail(user);
//    }

//    /**
//     * {@code GET  /activate} : activate the registered user.
//     *
//     * @param key the activation key.
//     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
//     */
//    @GetMapping("/activate")
//    public ResponseEntity<String>  activateAccount(@RequestParam(value = "key") String key) {
//        try{
//            Optional<User> user = userService.activateRegistration(key);
//            if (!user.isPresent()) {
//                throw new UserException("No user was found for this activation key");
//            }
//            return new ResponseEntity<>("the account has been activated", HttpStatus.OK);
//        }catch (UserException ex){
//            return new ResponseEntity<>(
//                    String.format("Error! %s", ex.getMessage()),
//                    HttpStatus.NOT_FOUND);
//        }
//
//    }



}
