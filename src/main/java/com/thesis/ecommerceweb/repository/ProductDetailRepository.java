package com.thesis.ecommerceweb.repository;

import com.thesis.ecommerceweb.model.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
    List<ProductDetail> getProductDetailByProduct_PidAndSize_Sid(int pid, int sid);

}
