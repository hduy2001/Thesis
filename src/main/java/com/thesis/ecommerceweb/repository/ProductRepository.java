package com.thesis.ecommerceweb.repository;

import com.thesis.ecommerceweb.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByCategory_Cid(int id);
    List<Product> findAllByGenderAndCategory_Cid(String gender, int id);
    List<Product> findAllByGenderAndBrandAndCategory_Cid(String gender, String brand, int id);
}
