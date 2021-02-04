package com.agakoz.physf.services;


import com.agakoz.physf.Authentication.IAuthenticationFacade;
import com.agakoz.physf.model.DTO.*;
import com.agakoz.physf.model.User;
import com.agakoz.physf.repositories.UserRepository;
import com.agakoz.physf.security.SecurityUtils;
import com.agakoz.physf.services.exceptions.*;
import com.agakoz.physf.utils.ObjectMapperUtils;
import io.github.jhipster.security.RandomUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private static UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private IAuthenticationFacade authenticationFacade;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, IAuthenticationFacade iAuthenticationFacade) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationFacade = iAuthenticationFacade;
    }

    public CurrentUserPersonalDTO getCurrentUserPersonalDTO() {
        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new UserException("Current user login not found"));
        Optional<CurrentUserPersonalDTO> user = userRepository.retrieveUserPersonalAsDTOById(currentUsername);
        if (user.isEmpty()) {
            throw new UserException("Błąd użytkownika");
        }
        return user.get();
    }

    public CurrentUserDTO getCurrentUserDTO() throws UserException {
        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new UserException("Current user login not found"));
        CurrentUserDTO currentUserDTO = userRepository.retrieveCurrentUserAsDTOByUsername(currentUsername);
        return currentUserDTO;

    }

    public static User getCurrentUser() throws UserException {
        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new UserException("Current user login not found"));
        Optional<User> user = userRepository.findByUsername(currentUsername);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserException("Current user login not found");
        }

    }

    public void updateCurrentUserAccount(LoginRequest userAccountDTO) throws UserException {
        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new UserException("Current user login not found"));
        Optional<User> userOpt = userRepository.findByUsername(currentUsername);
        if (userOpt.isPresent()) {
            validateUsername(userAccountDTO.getUsername());
            userOpt.get().setUsername(userAccountDTO.getUsername());
            String encodePassword = getEncodedPassword(userAccountDTO);
            userOpt.get().setPassword(encodePassword);
            userRepository.save(userOpt.get());
        }
    }

    public void updateCurrentUserPersonal(CurrentUserPersonalDTO userPersonalDTO) throws UserException {
        String currentUsername = SecurityUtils
                .getCurrentUserUsername()
                .orElseThrow(() -> new UserException("Current user login not found"));
        Optional<User> userOpt = userRepository.findByUsername(currentUsername);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            //TODO: validatePersonalInfo(userPersonalDTO);
            User updatedUser = ObjectMapperUtils.map(userPersonalDTO, user);
            userRepository.save(updatedUser);

        }
    }

    public void deleteCurrentUser() throws UserException {
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
//                            if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
//                                throw new InvalidPasswordException();
//                            }
                            String encryptedPassword = passwordEncoder.encode(newPassword);
                            user.setPassword(encryptedPassword);
                            log.debug("Changed password for User: {}", user);
                        }
                );
    }

    public boolean confirmPassword(String password) {
        System.out.println("confirming");
        Optional<String> username = SecurityUtils.getCurrentUserUsername();
        if (username.isPresent()) {
            Optional<User> user = userRepository.findByUsername(username.get());
            if (!user.isPresent()) {
                return false;
            }
            String currentEncryptedPassword = user.get().getPassword();
            return passwordEncoder.matches(password, currentEncryptedPassword);
        } else {
            return false;
        }

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
        newUser.setCreatedDate(LocalDate.now());
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
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

    private void validateUsername(String username) throws UsernameIsNullException, UsernameAlreadyUsedException, UsernameTooShortException {
        if (username == null) throw new UsernameIsNullException();
        Optional<User> userByUsername = userRepository.findByUsername(username);
        if (userByUsername.isPresent()) {
            String currentUsername = SecurityUtils
                    .getCurrentUserUsername()
                    .orElseThrow(() -> new UserException("Current user login not found"));
            if (userByUsername.get().equals(userRepository.findByUsername(currentUsername).get()))
                throw new UsernameAlreadyUsedException(username);
        }
        if (username.length() < 4)
            throw new UsernameTooShortException();
    }

    private String getEncodedPassword(LoginRequest userAccountDTO) {
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

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
                .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
                .forEach(
                        user -> {
                            log.debug("Deleting not activated user {}", user.getUsername());
                            userRepository.delete(user);

                        }
                );
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
