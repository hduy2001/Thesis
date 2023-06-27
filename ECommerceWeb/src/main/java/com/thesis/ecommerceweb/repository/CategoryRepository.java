package com.thesis.ecommerceweb.repository;

import com.thesis.ecommerceweb.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

}
