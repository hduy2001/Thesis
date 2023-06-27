package com.thesis.ecommerceweb.service;

import com.thesis.ecommerceweb.model.Product;
import com.thesis.ecommerceweb.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }

    public void addProduct(Product product) {
        productRepository.save(product);
    }

    public void removeProductById(int id) {
        productRepository.deleteById(id);
    }

    public Optional<Product> getProductById(int id) {
        return productRepository.findById(id);
    }

    public List<Product> getAllProductsByCategoryId(int id) {
        return productRepository.findAllByCategory_Cid(id);
    }

    public List<Product> getAllProductsByGender(String gender, int id) {
        return productRepository.findAllByGenderAndCategory_Cid(gender, id);
    }

    public List<Product> getAllProductsByGenderAndBrand(String gender, String brand, int id) {
        return productRepository.findAllByGenderAndBrandAndCategory_Cid(gender, brand, id);
    }

}
