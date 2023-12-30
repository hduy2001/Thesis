package com.thesis.ecommerceweb.service;

import com.thesis.ecommerceweb.dto.UserDTO;
import com.thesis.ecommerceweb.model.User;

public interface UserService {

    User save (UserDTO userDTO);

    User findUserByUsername(String username);

}
