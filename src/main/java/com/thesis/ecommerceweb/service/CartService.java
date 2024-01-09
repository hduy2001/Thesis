package com.thesis.ecommerceweb.service;

import com.thesis.ecommerceweb.model.Cart;
import com.thesis.ecommerceweb.repository.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    @Autowired
    CartRepository cartRepository;

    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }

    public Cart findExactlyCart(int pid, String username, String size) {
        return cartRepository.findByPidAndUsernameAndSize(pid, username, size);
    }

    public List<Cart> findAllCartByUsername(String username) {
        return cartRepository.findCartByUsername(username);
    }

    @Transactional
    public Cart removeItem(int id) {
        Cart cart = cartRepository.findCartByCartId(id);
        if (cart != null) {
            if (cart.getQuantity() > 1) {
                cart.setQuantity(cart.getQuantity() - 1);
                cartRepository.save(cart);
            } else {
                cartRepository.deleteCartByCartId(id);
            }
        }
        return cart;
    }

    @Transactional
    public void markAndRemoveCompleteCarts(List<Cart> cartList) {
        for (Cart cart : cartList) {
            cart.setComplete(true);
            cartRepository.save(cart);
        }

        cartRepository.deleteByCompleteTrue();
    }

}
