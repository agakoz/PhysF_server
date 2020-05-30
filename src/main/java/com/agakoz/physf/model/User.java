package com.agakoz.physf.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
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

    @Column(nullable = false, length = 255)
    @NonNull
    private String username;

    @NonNull
    @Column(nullable = false, length = 255)
    private String password;

    @NonNull
    @Column(nullable = false, length = 255)
    private String role;

    @Column
    @NonNull
    String name;

    @Column
    @NonNull
    String surname;

    @Column
    @NonNull
    String company;

    @Column
    @NonNull
    String address;

    @Column
    @NonNull
    String city;

    @Column(name="licence_number", length = 50)
    @NonNull
    String licenceNumber;

    @Column
    @NonNull
    String specializations;

    @Column(name = "professional_title")
    @NonNull
    String professionalTitle;

    @Column(name="birth_date")
    @NonNull
    Date birthDate;

    @Column(length = 320)
    @NonNull
    String email;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
