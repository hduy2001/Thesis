package com.thesis.ecommerceweb.service;

import com.thesis.ecommerceweb.dto.UserDTO;
import com.thesis.ecommerceweb.model.User;
import com.thesis.ecommerceweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User save(UserDTO userDTO) {
        User user = new User(userDTO.getUsername(), passwordEncoder.encode(userDTO.getPassword()), userDTO.getName(), userDTO.getEmail(), userDTO.getPhoneNumber(), userDTO.getAddress(), userDTO.getRole(), userDTO.getVerificationCode(), userDTO.isConfirm());
        return userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }


}
