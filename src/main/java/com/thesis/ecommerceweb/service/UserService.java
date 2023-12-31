package com.thesis.ecommerceweb.service;

import com.thesis.ecommerceweb.dto.UserDTO;
import com.thesis.ecommerceweb.model.User;

public interface UserService {

    User save (UserDTO userDTO, String path);

    User findUserByUsername(String username);

    public void sendEmail(User user, String path);

    public boolean verifyAccount(String verificationCode);
}
