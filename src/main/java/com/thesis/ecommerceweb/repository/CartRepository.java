package com.thesis.ecommerceweb.repository;

import com.thesis.ecommerceweb.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    List<Cart> findCartByUsername(String username);
    Cart findByPidAndUsernameAndSize(int pid, String username, String size);
    List<Cart> deleteByCompleteTrue();
    void deleteCartByCartId(int id);
    Cart findCartByCartId(int id);

}
