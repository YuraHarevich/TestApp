package com.example.TestApp.DTO;

import com.example.TestApp.entity.Product;
import com.example.TestApp.entity.Sku;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private int id;

    private String name;

    private String countryOfProduction;

    private boolean active;

    private List<SkuDTO> skus;

    public Product toProduct() {
        Product product = new Product();
        product.setId(id);
        product.setCountryOfProduction(countryOfProduction);
        product.setName(name);
        product.setActive(active);
        product.setSkus(skus==null?null:skus.stream().map(skuDTO -> {
            return skuDTO.toSku(product);
        }).collect(Collectors.toList()));
        return product;
    }
}
