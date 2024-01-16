package com.thesis.ecommerceweb.service;

import com.thesis.ecommerceweb.dto.UserDTO;
import com.thesis.ecommerceweb.model.User;

import java.util.List;

public interface UserService {
    User updateUser(UserDTO userDTO);

    List<User> saveSample();

    User save (UserDTO userDTO, String path);

    User findUserByUsername(String username);

    void sendEmail(User user, String path);

    boolean verifyAccount(String verificationCode);

    User updatePassword(String username, String confirmPassword);

    void sendEmailGetPassword(String email, String path);
}
