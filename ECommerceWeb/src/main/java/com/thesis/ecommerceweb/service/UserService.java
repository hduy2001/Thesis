package com.thesis.ecommerceweb.service;

import com.thesis.ecommerceweb.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> saveAll(List<User> entities);

    List<User> findAll();

    List<User> findAllById(List<String> integers);

    User save(User entity);

    Optional<User> findById(String id);

    boolean existsById(String integer);

    long count();

    void deleteById(String id);

    void delete(User entity);

    void deleteAll(List<User> entities);

    void deleteAll();

    boolean checkLogin(String username, String password);

    boolean checkRegister(String username);


}
