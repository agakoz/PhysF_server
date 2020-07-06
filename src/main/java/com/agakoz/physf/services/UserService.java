package com.agakoz.physf.services;


import com.agakoz.physf.model.DTO.UserDTO;
import com.agakoz.physf.model.Patient;
import com.agakoz.physf.model.User;
import com.agakoz.physf.repositories.UserRepository;
import com.agakoz.physf.services.exceptions.EmailAlreadyUsedException;
import com.agakoz.physf.services.exceptions.UserException;
import com.agakoz.physf.services.exceptions.UsernameAlreadyUsedException;
import io.github.jhipster.security.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public List<UserDTO> getAllUsers() throws UserException {
        List<UserDTO> users = userRepository.retrieveAllUserAsDTO();
        if (users.isEmpty()) {
            throw new UserException("No users.");
        } else {
            return users;
        }
    }

    public UserDTO getUserById(int id) throws UserException {

        Optional<UserDTO> userOpt = userRepository.retrieveUserAsDTOById(id);
        if (userOpt.isPresent())
            return userOpt.get();
        else throw new UserException(String.format("User with id = %d not found", id));

    }


    public void updateUser(int id, User user) throws UserException {
        userExistsOrThrow(id);
        userRepository.save(user);

    }

    public void deleteUserById(int id) throws UserException {

        userRepository.delete(getExistingUser(id));
    }

    private String getEncodedPassword(User user) {
        String password = user.getPassword();
        return passwordEncoder.encode(password);
    }

    private boolean userExists(int id) {
        Optional<User> user = userRepository.findById(id);
        return user.isPresent();
    }

    private void userExistsOrThrow(int id) throws UserException {
        if (!userExists(id))
            throw new UserException(String.format("No user with id=\"%d\".", id));
    }

    private User getExistingUser(int id) throws UserException {
        userExistsOrThrow(id);
        return userRepository.findById(id).get();

    }


}
