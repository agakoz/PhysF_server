package com.agakoz.physf.controllers;

import com.agakoz.physf.model.DTO.JwtResponse;
import com.agakoz.physf.model.DTO.LoginRequest;
import com.agakoz.physf.model.DTO.PasswordDTO;
import com.agakoz.physf.model.DTO.UserCreateDTO;
import com.agakoz.physf.model.User;
import com.agakoz.physf.security.JwtUtils;
import com.agakoz.physf.security.UserDetailsServiceImpl;
import com.agakoz.physf.services.MailService;
import com.agakoz.physf.services.UserService;
import com.agakoz.physf.utils.ObjectMapperUtils;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final MailService mailService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;


    public AuthController(UserService userService, MailService mailService, AuthenticationManager authenticationManager, UserDetailsServiceImpl userDetailsService, JwtUtils jwt) {
        this.userService = userService;
        this.mailService = mailService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwt;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public void registerAccount(@Valid @RequestBody UserCreateDTO userCreateDTO) {
//        System.out.println("rejestracja");
        System.out.println(userCreateDTO);
        User user = userService.registerUser(userCreateDTO);
        mailService.sendActivationEmail(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        User userDetails = (User) authentication.getPrincipal();
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(item -> item.getAuthority())
//                .collect(Collectors.toList());

        JwtResponse jwtResponse = ObjectMapperUtils.map(userDetails, new JwtResponse());
        jwtResponse.setToken(jwt);
//        System.out.println(jwtResponse);
        return ResponseEntity.ok(jwtResponse);
    }


    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));


        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @PostMapping("/confirmPassword")
    public ResponseEntity<Object> checkPassword(@RequestBody PasswordDTO passwordDTO) {
        String password = passwordDTO.getPassword();
        boolean confirmed = userService.confirmPassword(password);
        return new ResponseEntity<>(confirmed, HttpStatus.OK);
    }
}
