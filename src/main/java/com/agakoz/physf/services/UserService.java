package com.agakoz.physf.services;


import com.agakoz.physf.model.User;
import com.agakoz.physf.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public void createAndAddUser(User user) {

        String encodePassword = getEncodedPassword(user);
        user.setPassword(encodePassword);

         user.setRole("ROLE_USER");

        userRepository.save(user);
    }

    private String getEncodedPassword(User user) {
        String password = user.getPassword();
        return passwordEncoder.encode(password);
    }

}
