package com.thesis.ecommerceweb.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "sizes")
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sid;

    @Column(name = "size", length = 5)
    private String size;
}
