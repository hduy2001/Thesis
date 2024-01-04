package com.thesis.ecommerceweb.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "stock")
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sid")
    private int sid;

    @Column(name = "cid")
    private int cid;

    @Column(name = "in_stock")
    private int inStock;

    @Column(name = "pid")
    private int pid;

    @Column(name = "size")
    private String size;
}
