package com.agakoz.physf.repositories;


import com.agakoz.physf.model.DTO.CurrentUserAccountDTO;
import com.agakoz.physf.model.DTO.CurrentUserDTO;
import com.agakoz.physf.model.DTO.CurrentUserPersonalDTO;
import com.agakoz.physf.model.DTO.UserDTO;
import com.agakoz.physf.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(@Param("username") String username);

    Optional<User> findById(@Param("id") int id);

    @Query("SELECT new com.agakoz.physf.model.DTO.UserDTO(u.id, u.username, u.password, u.role, u.name, u.surname, u.company, u.address, u.city, u.licenceNumber, u.specializations, u.professionalTitle, u.birthDate, u.email, u.activated, u.activationKey, u.resetKey) FROM User u")
    List<UserDTO> retrieveAllUserAsDTO();

    @Query("SELECT new com.agakoz.physf.model.DTO.UserDTO(u.id, u.username, u.password, u.role, u.name, u.surname, u.company, u.address, u.city, u.licenceNumber, u.specializations, u.professionalTitle, u.birthDate, u.email, u.activated, u.activationKey, u.resetKey) FROM User u WHERE u.id=:id")
    Optional<UserDTO> retrieveUserAsDTOById(@Param("id") int id);

    @Query("SELECT new com.agakoz.physf.model.DTO.CurrentUserDTO(u.id, u.username, u.name, u.surname, u.company, u.address, u.city, u.licenceNumber, u.specializations, u.professionalTitle, u.birthDate, u.email, u.activated) FROM User u WHERE u.username=:username")
    CurrentUserDTO retrieveCurrentUserAsDTOByUsername(@Param("username") String username);

    @Query("SELECT new com.agakoz.physf.model.DTO.CurrentUserPersonalDTO(u.name, u.surname, u.company, u.address, u.city, u.licenceNumber, u.specializations, u.professionalTitle, u.birthDate, u.email ) FROM User u WHERE u.id = :userId")
    CurrentUserPersonalDTO retrieveUserPersonalAsDTOById(@Param("userId") int userId);

    @Query("SELECT new com.agakoz.physf.model.DTO.CurrentUserAccountDTO(u.username, u.password ) FROM User u WHERE u.id = :userId")
    CurrentUserAccountDTO retrieveUserAccountAsDTOByUserId(@Param("userId") int userId);

    Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Optional<Object> findOneByEmailIgnoreCase(String email);

    @Query("DELETE FROM User u WHERE u.username=:currentUsername")
    void deleteByUsername(Optional<String> currentUsername);

    Optional<User> findOneByActivationKey(String activationKey);

    List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);
}
