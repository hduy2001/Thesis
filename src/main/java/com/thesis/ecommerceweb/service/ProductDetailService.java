package com.thesis.ecommerceweb.service;

import com.thesis.ecommerceweb.model.ProductDetail;
import com.thesis.ecommerceweb.repository.ProductDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDetailService {
    @Autowired
    ProductDetailRepository productDetailRepository;

    public List<ProductDetail> getProductDetails() {
        return productDetailRepository.findAll();
    }
}
