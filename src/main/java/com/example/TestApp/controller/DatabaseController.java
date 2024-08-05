package com.example.TestApp.controller;

import com.example.TestApp.DTO.ProductDTO;
import com.example.TestApp.entity.Product;
import com.example.TestApp.service.DatabaseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("db")
public class DatabaseController {
    private final DatabaseService service;

    public DatabaseController(DatabaseService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public void save(@RequestBody ProductDTO productDTO){
        service.save(productDTO.toProduct());
    }
    @GetMapping("/get")
    public List<ProductDTO> get(){
        return service.getAll()
                .stream()
                .map(Product::toProductDTO)
                .collect(Collectors.toList());
    }
}
