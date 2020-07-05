package com.agakoz.physf.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.validation.constraints.Email;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @NonNull
    int id;

    @Column(nullable = false, length = 255, unique = true)
    @NonNull
    private String username;

    @NonNull
    @Column(nullable = false, length = 255)
    private String password;

    @NonNull
    @Column(nullable = false, length = 255)
    private String role;

    @Column
    String name;

    @Column
    String surname;

    @Column
    String company;

    @Column
    String address;

    @Column
    String city;

    @Column(name="licence_number", length = 50, unique = true)
    String licenceNumber;

    @Column
    String specializations;

    @Column(name = "professional_title")
    String professionalTitle;

    @Column(name="birth_date")
    Date birthDate;

    @Column(length = 320, unique = true)
    @Email
    String email;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

          return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
