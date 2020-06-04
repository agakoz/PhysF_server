package com.agakoz.physf.services;


import com.agakoz.physf.model.DTO.CurrentUserAccountDTO;
import com.agakoz.physf.model.DTO.CurrentUserDTO;
import com.agakoz.physf.model.DTO.CurrentUserPersonalDTO;
import com.agakoz.physf.model.User;
import com.agakoz.physf.repositories.UserRepository;
import com.agakoz.physf.services.exceptions.CurrentUserException;
import com.agakoz.physf.services.exceptions.UsernameAlreadyUsedException;
import com.agakoz.physf.services.exceptions.UsernameIsNullException;
import com.agakoz.physf.services.exceptions.UsernameTooShortException;
import com.agakoz.physf.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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


    public CurrentUserDTO getCurrentUserDTO() throws CurrentUserException {
        CurrentUserDTO currentUserDTO = userRepository.retrieveCurrentUserAsDTOById(getCurrentUser().getId());
        return currentUserDTO;

    }


    public void updateCurrentUserAccount(CurrentUserAccountDTO userAccountDTO) throws CurrentUserException {
        Optional<User> user = userRepository.findById(getCurrentUser().getId());
        if (user.isPresent()) {
            validateUsername(userAccountDTO.getUsername());
            user.get().setUsername(userAccountDTO.getUsername());
            String encodePassword = getEncodedPassword(userAccountDTO);
            user.get().setPassword(encodePassword);
            userRepository.save(user.get());
        }
    }

    public void updateCurrentUserPersonal(CurrentUserPersonalDTO userPersonalDTO) throws CurrentUserException {
        Optional<User> userOpt = userRepository.findById(getCurrentUser().getId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            //TODO: validatePersonalInfo(userPersonalDTO);
            User updatedUser = ObjectMapperUtils.map(userPersonalDTO, user);
            userRepository.save(updatedUser);

        }
    }

    public void deleteUser() throws CurrentUserException {
        userRepository.delete(getCurrentUser());
    }

    private void validateUsername(String username) throws UsernameIsNullException, UsernameAlreadyUsedException, UsernameTooShortException {
        if (username == null) throw new UsernameIsNullException();
        Optional<User> userByUsername = userRepository.findByUsername(username);
        if (userByUsername.isPresent()) {
            if (userByUsername.get().getId() != getCurrentUser().getId())
                throw new UsernameAlreadyUsedException(username);
        }
        if (username.length() < 4)
            throw new UsernameTooShortException();
    }


    private User getCurrentUser() throws CurrentUserException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User currentUser;
        if (principal instanceof UserDetails) {
            currentUser = ((User) (principal));

        } else {
            throw new CurrentUserException();

        }
        return currentUser;
    }

    private String getEncodedPassword(CurrentUserAccountDTO userAccountDTO) {
        String password = userAccountDTO.getPassword();
        return passwordEncoder.encode(password);
    }
}
