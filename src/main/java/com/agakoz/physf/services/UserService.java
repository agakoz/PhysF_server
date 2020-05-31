package com.agakoz.physf.services;


import com.agakoz.physf.model.DTO.UserDTO;
import com.agakoz.physf.model.Patient;
import com.agakoz.physf.model.User;
import com.agakoz.physf.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // TODO  user validation
    public void createAndAddUser(User user) throws  IOException {

        String encodePassword = getEncodedPassword(user);
        user.setPassword(encodePassword);

        user.setRole("ROLE_USER");

        userRepository.save(user);
    }

    private String getEncodedPassword(User user) {
        String password = user.getPassword();
        return passwordEncoder.encode(password);
    }

    public List<UserDTO> getAllUsers() throws IOException {
        List<UserDTO> users = userRepository.retrieveAllUserAsDTO();
        if (users.isEmpty()) {
            throw new IOException("No users.");
        } else {
            return users;
        }
    }

    public UserDTO getUserById(int id) throws IOException{
        if(userExists(id))
            return userRepository.retrieveUserAsDTOById(id);
        else throw new IOException(String.format("No user with id=\"%d\".", id));
    }


    public void updateUser(int id, Patient patient) throws IOException {
        userRepository.save(getExistingUser(id));
    }

    public void deleteUser(int id) throws IOException {

        userRepository.delete(getExistingUser(id));
    }

    public boolean userExists(int id) {
        Optional<User> user = userRepository.findById(id);
        return user.isPresent();
    }

    public User getExistingUser(int id) throws IOException {
        if (userExists(id)) {
            return userRepository.findById(id).get();
        } else {
            throw new IllegalArgumentException(String.format("user with id: \"%d\" does not exist ", id));
        }
    }
}
