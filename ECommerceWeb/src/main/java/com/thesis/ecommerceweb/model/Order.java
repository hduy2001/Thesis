package com.thesis.ecommerceweb.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int oid;

    private String username;

    private int total_price;
    private int quantity;

    private String pay_type;
    @Column(columnDefinition = "TINYINT(1) DEFAULT 0")
    private Integer is_pay;
    @Column(name = "status")
    private String status;
}
