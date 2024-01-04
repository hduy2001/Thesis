package com.thesis.ecommerceweb.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cart_id")
    private int cartId;

    @Column(name = "pid")
    private int pid;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "size")
    private String size;

    @Column(name = "username")
    private String username;

    @Column(name = "complete")
    private boolean complete;
}
