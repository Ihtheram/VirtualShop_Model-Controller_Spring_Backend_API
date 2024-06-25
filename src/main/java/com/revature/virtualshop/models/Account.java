package com.revature.virtualshop.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Table(name = "accounts")
public class Account {

    @Id
    @Column(name = "account_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "image")
    private String image;

    @Column(name = "role", nullable = false)
    private String role = "USER";

    @Column(name = "status", nullable = false)
    private String status = "ACTIVE";

}
