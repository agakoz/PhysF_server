package com.agakoz.physf.repositories;


import com.agakoz.physf.model.DTO.UserDTO;
import com.agakoz.physf.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(@Param("username") String username);

    Optional<User> findById(@Param("id")int id);

    @Query("SELECT new com.agakoz.physf.model.DTO.UserDTO(u.id, u.username, u.password, u.role, u.name, u.surname, u.company, u.address, u.city, u.licenceNumber, u.specializations, u.professionalTitle, u.birthDate, u.email) FROM User u")
    List<UserDTO> retrieveAllUserAsDTO();

    @Query("SELECT new com.agakoz.physf.model.DTO.UserDTO(u.id, u.username, u.password, u.role, u.name, u.surname, u.company, u.address, u.city, u.licenceNumber, u.specializations, u.professionalTitle, u.birthDate, u.email) FROM User u WHERE u.id=:id")
    UserDTO retrieveUserAsDTOById(@Param("id")int id);
}
