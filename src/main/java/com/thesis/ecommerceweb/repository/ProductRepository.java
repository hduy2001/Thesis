package com.thesis.ecommerceweb.repository;

import com.thesis.ecommerceweb.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findAllByCategory_Cid(int id);
    List<Product> findAllByGenderAndCategory_Cid(String gender, int id);
    List<Product> findAllByGenderAndBrandAndCategory_Cid(String gender, String brand, int id);
    List<Product> findAllByBrand(String brand);

    @Query("SELECT product FROM Product product where product.name LIKE %?1%")
    List<Product> search(String keyword);

    @Query("SELECT DISTINCT p.color FROM Product p WHERE p.color IS NOT NULL")
    List<String> findAllColors();

    @Query("SELECT DISTINCT p.brand FROM Product p WHERE p.brand IS NOT NULL")
    List<String> findAllBrands();

}
