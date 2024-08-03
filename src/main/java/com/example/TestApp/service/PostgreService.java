package com.example.TestApp.service;

import com.example.TestApp.entity.Product;
import com.example.TestApp.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class PostgreService {
    private final ProductRepository repository;

    public PostgreService(ProductRepository repository) {
        this.repository = repository;
    }
    public void save(Product product){
        repository.save(product);
    }
    public void get(){
        repository.findAll();
    }
}
