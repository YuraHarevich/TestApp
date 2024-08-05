package com.example.TestApp.controller;

import com.example.TestApp.DTO.ProductDTO;
import com.example.TestApp.entity.Product;
import com.example.TestApp.service.DatabaseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AppController {
    private final DatabaseService databaseService;

    public AppController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @PostMapping("/save")
    public void save(@RequestBody ProductDTO productDTO){
        databaseService.save(productDTO.toProduct());
    }
    @GetMapping("/get")
    public List<ProductDTO> get(){
        return databaseService.get()
                .stream()
                .map(Product::toProductDTO)
                .collect(Collectors.toList());
    }
}

