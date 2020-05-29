package com.agakoz.physf.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @NonNull
    int id;

    @Column
    @NonNull
    String name;

    @Column
    @NonNull
    String surname;

    @Column
    @NonNull
    String companyName;

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false)
    private Account account;

    @Column(name="birth_date")
    @NonNull
    Date birthDate;

}
