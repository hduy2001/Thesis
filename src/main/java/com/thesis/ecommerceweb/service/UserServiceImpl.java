package com.thesis.ecommerceweb.service;

import com.thesis.ecommerceweb.model.User;
import com.thesis.ecommerceweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> saveAll(List<User> entities) {
        return (List<User>)userRepository.saveAll(entities);
    }

    @Override
    public List<User> findAll() {
        return (List<User>)userRepository.findAll();
    }

    @Override
    public List<User> findAllById(List<String> integers) {
        return (List<User>)userRepository.findAllById(integers);
    }

    @Override
    public User save(User entity) {
        return userRepository.save(entity);
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean existsById(String integer) {
        return userRepository.existsById(integer);
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public void delete(User entity) {
        userRepository.delete(entity);
    }

    @Override
    public void deleteAll(List<User> entities) {
        userRepository.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        userRepository.deleteAll();
    }

    @Override
    public boolean checkLogin(String username, String password) {
        Optional<User> optionalUser = findById(username);
        if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkRegister(String username) {
        Optional<User> optionalUser = findById(username);
        if (!optionalUser.isPresent()) {
            return true;
        }
        return false;
    }

}
