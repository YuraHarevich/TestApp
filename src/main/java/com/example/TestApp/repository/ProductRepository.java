package com.example.TestApp.repository;

import com.example.TestApp.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    Product findFirstByNameOrderByIdDesc(String name);
}
