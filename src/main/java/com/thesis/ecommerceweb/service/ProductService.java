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

    public List<Product> getAllProductsByCategoryId(int id, String keyword) {
        if (keyword != null) {
            return productRepository.search(keyword);
        }
        return productRepository.findAllByCategory_Cid(id);
    }

    public List<Product> getAllProductsByGender(String gender, int id) {
        return productRepository.findAllByGenderAndCategory_Cid(gender, id);
    }

    public List<Product> getAllProductsByGenderAndBrand(String gender, String brand, int id) {
        return productRepository.findAllByGenderAndBrandAndCategory_Cid(gender, brand, id);
    }

    public List<Product> getAllProductsByBrand(String brand) {
        return productRepository.findAllByBrand(brand);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.search(keyword);
    }

    public List<String> getAllColors() {
        List<String> colors = productRepository.findAllColors();
        colors.replaceAll(color -> color.equals("cerulean") ? "#007BA7" : color);
        colors.replaceAll(color -> color.equals("fuchsia dream") ? "#915C83" : color);
        colors.replaceAll(color -> color.equals("mint foam") ? "#AAF0D1" : color);
        colors.replaceAll(color -> color.equals("noise aqua") ? "#21ABCD" : color);
        colors.replaceAll(color -> color.equals("ale brown") ? "#CC9966" : color);
        colors.replaceAll(color -> color.equals("siren red") ? "#EA3C53" : color);
        colors.replaceAll(color -> color.equals("valerian blue") ? "#5D8AA8" : color);
        colors.replaceAll(color -> color.equals("steel") ? "#B0C4DE" : color);
        colors.replaceAll(color -> color.equals("blue dawn") ? "#F0F8FF" : color);
        colors.replaceAll(color -> color.equals("sand") ? "#EDC9AF" : color);
        colors.replaceAll(color -> color.equals("dusklight") ? "#FF8C00" : color);
        colors.replaceAll(color -> color.equals("islantis") ? "#54626F" : color);
        colors.replaceAll(color -> color.equals("kingstone") ? "#808A87" : color);
        colors.replaceAll(color -> color.equals("blackinferrno") ? "#253529" : color);
        colors.replaceAll(color -> color.equals("mint canary") ? "#00FFCD" : color);
        colors.replaceAll(color -> color.equals("mint") ? "#3EB489" : color);
        colors.replaceAll(color -> color.equals("blaze") ? "#E68FAC" : color);
        colors.replaceAll(color -> color.equals("black red") ? "#7C0A02" : color);
        colors.replaceAll(color -> color.equals("blueberry") ? "#4F86F7" : color);
        return colors;
    }

    public List<String> getAllBrands() {
        return productRepository.findAllBrands();
    }
}
