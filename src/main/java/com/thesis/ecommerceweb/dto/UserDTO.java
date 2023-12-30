package com.thesis.ecommerceweb.dto;

import lombok.Data;

@Data
public class UserDTO {
    private String username;
    private String password;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String role;
    private String verificationCode;
    private boolean isConfirm;

    public UserDTO(String username, String password, String name, String email, String phoneNumber, String address, String role, String verificationCode, boolean isConfirm) {
        super();
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.isConfirm = isConfirm;
        this.role = role;
        this.verificationCode = verificationCode;
    }
}
