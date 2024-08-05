package com.example.TestApp.DTO;

import com.example.TestApp.entity.Product;
import com.example.TestApp.entity.Sku;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SkuDTO {
    private int id;

    private String code;

    private String color;

    private double price;

    @Override
    public String toString() {
        return "SkuDTO{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", color='" + color + '\'' +
                ", price=" + price +
                '}';
    }

    public Sku toSku(Product product){
        return new Sku(id,code,color,price,product);
    }
}
