package com.example.TestApp.entity;

import com.example.TestApp.DTO.ProductDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String countryOfProduction;

    @Column(nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "product", cascade = CascadeType.PERSIST)
    private List<Sku> skus;

    public ProductDTO toProductDTO(){
        ProductDTO productDto = new ProductDTO();
        productDto.setId(id);
        productDto.setName(name);
        productDto.setCountryOfProduction(countryOfProduction);
        productDto.setActive(active);
        productDto.setSkus(skus==null?null:skus.stream().map(Sku::toSkuDTO).collect(Collectors.toList()));
        return productDto;
    }
}