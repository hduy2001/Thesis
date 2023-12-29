package com.thesis.ecommerceweb.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "productDetail")
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pid", referencedColumnName = "pid")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sid", referencedColumnName = "sid")
    private Size size;

    private int stock;
}
