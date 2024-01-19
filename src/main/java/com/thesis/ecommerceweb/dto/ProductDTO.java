package com.thesis.ecommerceweb.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class ProductDTO {
    private int pid;
    private String name;
    private String brand;
    private String description;
    private int price;
    private String image;
    private String color;
    private String gender;
    private int cid;
    private String size;
    private Double rating;
    private Integer ratingCount;
    private int quantity;
}
