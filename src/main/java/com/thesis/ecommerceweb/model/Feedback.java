package com.thesis.ecommerceweb.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "feedbacks")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int fid;

    @Column(name = "username")
    private String username;

    @Column(name = "pid")
    private int pid;

    @Column(name = "rating")
    private double rating;

    @Column(name = "time_Stamp")
    private long timeStamp;
}
