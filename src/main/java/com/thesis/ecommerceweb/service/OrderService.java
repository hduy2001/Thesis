package com.thesis.ecommerceweb.service;

import com.thesis.ecommerceweb.model.Order;
import com.thesis.ecommerceweb.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;

    public void addOrder(Order order) {
        orderRepository.save(order);
    }

    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

    public void removeOrderById(int id) {
        orderRepository.deleteById(id);
    }

    public Optional<Order> getOrderById(int id) {
        return orderRepository.findById(id);
    }

    public List<Order> getAllOrderByUsername(String username, String status) {
        return orderRepository.findAllByUsernameAndStatus(username, status);
    }
}
