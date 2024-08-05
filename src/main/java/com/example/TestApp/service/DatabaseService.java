package com.example.TestApp.service;

import com.example.TestApp.entity.Product;
import com.example.TestApp.entity.Sku;
import com.example.TestApp.exception.SaveException;
import com.example.TestApp.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseService {
    private final ProductRepository repository;
    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);
    public DatabaseService(ProductRepository repository) {
        this.repository = repository;
    }
    public void save(Product product){
        if(product.getSkus()==null) {
            logger.error("Exception while saving product with id {}, product.sku == null", product.getId());
            throw new SaveException("Cant save product cause product.sku==null");
        }
        for(Sku sku: product.getSkus()){
            sku.setProduct(product);
        }
        repository.save(product);
    }

    public List<Product> get(){
        return repository.findAll();
    }
}
