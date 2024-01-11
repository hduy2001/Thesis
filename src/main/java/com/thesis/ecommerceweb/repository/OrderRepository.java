package com.thesis.ecommerceweb.repository;

import com.thesis.ecommerceweb.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByUsernameAndStatus(String username, String status);
}
