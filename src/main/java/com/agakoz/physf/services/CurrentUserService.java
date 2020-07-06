package com.agakoz.physf.services;


import com.agakoz.physf.Authentication.IAuthenticationFacade;
import com.agakoz.physf.model.DTO.CurrentUserAccountDTO;
import com.agakoz.physf.model.DTO.CurrentUserDTO;
import com.agakoz.physf.model.DTO.CurrentUserPersonalDTO;
import com.agakoz.physf.model.DTO.UserCreateDTO;
import com.agakoz.physf.model.User;
import com.agakoz.physf.repositories.UserRepository;
import com.agakoz.physf.security.SecurityUtils;
import com.agakoz.physf.services.exceptions.*;
import com.agakoz.physf.utils.ObjectMapperUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jhipster.security.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CurrentUserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private IAuthenticationFacade authenticationFacade;

    @Autowired
    public CurrentUserService(UserRepository userRepository, PasswordEncoder passwordEncoder, IAuthenticationFacade iAuthenticationFacade) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationFacade = iAuthenticationFacade;
    }


    public CurrentUserDTO getCurrentUserDTO() throws CurrentUserException {
        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new CurrentUserException("Current user login not found"));
        CurrentUserDTO currentUserDTO = userRepository.retrieveCurrentUserAsDTOByUsername(currentUsername);
        return currentUserDTO;

    }


    public void updateCurrentUserAccount(CurrentUserAccountDTO userAccountDTO) throws CurrentUserException {
        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new CurrentUserException("Current user login not found"));
        Optional<User> userOpt = userRepository.findByUsername(currentUsername);
        if (userOpt.isPresent()) {
            validateUsername(userAccountDTO.getUsername());
            userOpt.get().setUsername(userAccountDTO.getUsername());
            String encodePassword = getEncodedPassword(userAccountDTO);
            userOpt.get().setPassword(encodePassword);
            userRepository.save(userOpt.get());
        }
    }

    public void updateCurrentUserPersonal(CurrentUserPersonalDTO userPersonalDTO) throws CurrentUserException {
        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new CurrentUserException("Current user login not found"));
        Optional<User> userOpt = userRepository.findByUsername(currentUsername);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            //TODO: validatePersonalInfo(userPersonalDTO);
            User updatedUser = ObjectMapperUtils.map(userPersonalDTO, user);
            userRepository.save(updatedUser);

        }
    }

    public void deleteUser() throws CurrentUserException {
        userRepository.deleteByUsername(SecurityUtils.getCurrentUserUsername());
    }
    @Transactional
    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils
                .getCurrentUserUsername()
                .flatMap(userRepository::findByUsername)
                .ifPresent(
                        user -> {
                            String currentEncryptedPassword = user.getPassword();
                            if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                                throw new InvalidPasswordException();
                            }
                            String encryptedPassword = passwordEncoder.encode(newPassword);
                            user.setPassword(encryptedPassword);
                            log.debug("Changed password for User: {}", user);
                        }
                );
    }



    public User registerUser(UserCreateDTO userCreateDTO) {
        userRepository
                .findByUsername(userCreateDTO.getUsername().toLowerCase())
                .ifPresent(
                        existingUser -> {
                            boolean removed = removeNonActivatedUser(existingUser);
                            if (!removed) {
                                throw new UsernameAlreadyUsedException(userCreateDTO.getUsername());
                            }
                        }
                );
        userRepository
                .findOneByEmailIgnoreCase(userCreateDTO.getEmail())
                .ifPresent(
                        existingUser -> {

                                throw new EmailAlreadyUsedException();

                        }
                );

        String encryptedPassword = passwordEncoder.encode(userCreateDTO.getPassword());

        User newUser = ObjectMapperUtils.map(userCreateDTO, new User());

        newUser.setPassword(encryptedPassword);
        newUser.setRole("ROLE_USER");
        if (userCreateDTO.getEmail() != null) {
            newUser.setEmail(userCreateDTO.getEmail().toLowerCase());
        }

        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }


    private void validateUsername(String username) throws UsernameIsNullException, UsernameAlreadyUsedException, UsernameTooShortException {
        if (username == null) throw new UsernameIsNullException();
        Optional<User> userByUsername = userRepository.findByUsername(username);
        if (userByUsername.isPresent()) {
            String currentUsername = SecurityUtils
                    .getCurrentUserUsername()
                    .orElseThrow(() -> new CurrentUserException("Current user login not found"));
            if (userByUsername.get().equals(userRepository.findByUsername(currentUsername).get()))
                throw new UsernameAlreadyUsedException(username);
        }
        if (username.length() < 4)
            throw new UsernameTooShortException();
    }

    private String getEncodedPassword(CurrentUserAccountDTO userAccountDTO) {
        String password = userAccountDTO.getPassword();
        return passwordEncoder.encode(password);
    }

    private boolean removeNonActivatedUser(User existingUser) {
        if (existingUser.getActivated()) {
            return false;
        }
        userRepository.delete(existingUser);
        userRepository.flush();
        return true;
    }


    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);

        return userRepository
                .findOneByActivationKey(key)
                .map(
                        user -> {
                            // activate given user for the registration key.
                            user.setActivated(true);
                            user.setActivationKey(null);
userRepository.save(user);
                            log.debug("Activated user: {}", user);
                            return user;
                        }
                );
    }
}
