package com.thesis.ecommerceweb.service;

import com.thesis.ecommerceweb.model.Product;
import com.thesis.ecommerceweb.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<Product> getAllProductsByCategoryId(int id, String keyword, List<String> brand, String color) {
        if (keyword != null) {
            return productRepository.search(keyword);
        }

        if (brand.size()>0 && brand != null) {
            return productRepository.findAllByCategory_CidAndBrandIn(id, brand);
        }

        if (color != null) {
            return productRepository.findAllByCategory_CidAndColor(id, color);
        }

        return productRepository.findAllByCategory_Cid(id);
    }

    public List<Product> getAllProductsByGender(String gender, int id, String keyword, List<String> brand, String color) {
        if (keyword != null) {
            return productRepository.search(keyword);
        }

        if (brand.size()>0 && brand != null) {
            return productRepository.findAllByGenderAndCategory_CidAndBrandIn(gender, id, brand);
        }

        if (color != null) {
            return productRepository.findAllByGenderAndCategory_CidAndColor(gender, id, color);
        }

        return productRepository.findAllByGenderAndCategory_Cid(gender, id);
    }

    public List<Product> getAllProductsByGenderAndBrand(String gender, String brand, int id, String keyword, List<String> brands, String color) {
        if (keyword != null) {
            return productRepository.search(keyword);
        }

        if (brands.size()>0 && brand != null) {
            return productRepository.findAllByGenderAndCategory_CidAndBrandIn(gender, id, brands);
        }

        if (color != null) {
            return productRepository.findAllByGenderAndBrandAndCategory_CidAndColor(gender, brand, id, color);
        }
        return productRepository.findAllByGenderAndBrandAndCategory_Cid(gender, brand, id);
    }

    public List<Product> getAllProductsByBrand(String brand, String keyword, List<String> brands, String color) {
        if (keyword != null) {
            return productRepository.search(keyword);
        }

        if (brands.size()>0 && brand != null) {
            return productRepository.findAllByBrandIn(brands);
        }

        if (color != null) {
            return productRepository.findAllByBrandAndColor(brand, color);
        }
        return productRepository.findAllByBrand(brand);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepository.search(keyword);
    }

    public List<String> getAllColors() {
        return productRepository.findAllColors();
    }

    public List<String> getAllBrands() {
        return productRepository.findAllBrands();
    }

    public void saveRate(int id, double rate, int count) {
        Product existProduct = productRepository.findProductByPid(id);
        existProduct.setRating(rate);
        existProduct.setRatingCount(count);
        productRepository.save(existProduct);
    }

    public List<Product> getListProduct(List<Integer> ids) {
        List<Product> productList = new ArrayList<>();

        for (int i = 0; i < ids.size(); i++) {
            productList.add(productRepository.findProductByPid(ids.get(i)));
        }

        return productList;
    }

    public List<Product> getWomenList() {
        List<Product> productList = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        ids.add(97);
        ids.add(101);
        ids.add(113);
        ids.add(42);
        for (int i = 0; i < ids.size(); i++) {
            productList.add(productRepository.findProductByPid(ids.get(i)));
        }
        return productList;
    }

    public List<Product> getMenList() {
        List<Product> productList = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        ids.add(99);
        ids.add(100);
        ids.add(114);
        ids.add(1);
        for (int i = 0; i < ids.size(); i++) {
            productList.add(productRepository.findProductByPid(ids.get(i)));
        }
        return productList;
    }

    public List<Product> getShoesList(String gender) {
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            productList.add(productRepository.findAllByGenderAndCategory_CidOrderByRatingDesc(gender, 1).get(i));
        }
        return productList;
    }

    public List<Product> getAccessoriesList() {
        List<Product> productList = new ArrayList<>();
        productList.add(productRepository.findProductByCategory_CidOrderByRatingDesc(2).get(0));
        productList.add(productRepository.findProductByCategory_CidOrderByRatingDesc(3).get(0));
        productList.add(productRepository.findProductByCategory_CidOrderByRatingDesc(4).get(0));
        productList.add(productRepository.findProductByCategory_CidOrderByRatingDesc(5).get(0));
        productList.add(productRepository.findProductByCategory_CidOrderByRatingDesc(7).get(0));
        return productList;
    }
}
