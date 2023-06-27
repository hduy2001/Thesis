package com.thesis.ecommerceweb.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Data
@Table(name = "users")
public class User implements Serializable {
    @Id
    @Column(name="username", length = 50)
    private String username;

    @Column(name="password", nullable = false, length = 50)
    private String password;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "phone_number", length = 12, unique = true)
    private String phoneNumber;

    @Column(name = "address", nullable = false, length = 250)
    private String address;

    @Column(name = "isConfirm")
    private Integer isConfirm;
    public User(String username, String password, String name, String email, String phoneNumber, String address, Integer isConfirm) {
        super();
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.isConfirm = isConfirm;
    }

    public User() {
        super();
    }
}
