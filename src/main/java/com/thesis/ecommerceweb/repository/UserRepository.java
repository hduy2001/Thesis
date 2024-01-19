package com.thesis.ecommerceweb.repository;

import com.thesis.ecommerceweb.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findUserByUsername(String username);

    User findByVerificationCode(String code);

    User findUserByEmail(String email);

    int countAllByRole(String role);
}
