package com.thesis.ecommerceweb.repository;

import com.thesis.ecommerceweb.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}
