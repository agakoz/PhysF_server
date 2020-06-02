package com.agakoz.physf.services;


import com.agakoz.physf.model.DTO.UserDTO;
import com.agakoz.physf.model.User;
import com.agakoz.physf.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CurrentUserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public CurrentUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    private String getEncodedPassword() throws IOException {
        String password = getCurrentUser().getPassword();
        return passwordEncoder.encode(password);
    }

    public UserDTO getUser() throws IOException {

        return userRepository.retrieveUserAsDTOById(getCurrentUser().getId());

    }


    public void updateUser(User user) throws IOException {
       if(getCurrentUser().getId() == user.getId())
            userRepository.save(user);
       else
           throw new IOException("id of user to be updated is wrong!");
    }

    public void deleteUser() throws IOException {
        userRepository.delete(getCurrentUser());
    }




    private User getCurrentUser() throws IOException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User currentUser;
        if (principal instanceof UserDetails) {
            currentUser = ((User) (principal));

        } else {
            currentUser = null;

        }
        return currentUser;
    }
}
