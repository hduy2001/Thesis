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

    @Column(name = "detail")
    private String detail;

    @Column(name = "total_price")
    private int totalPrice;
    private int quantity;

    @Column(name = "pay_type")
    private String payType;
    @Column(columnDefinition = "TINYINT(1) DEFAULT 0", name = "is_pay")
    private int isPay;
    @Column(name = "status")
    private String status;
}
