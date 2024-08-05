package com.example.TestApp.entity;

import com.example.TestApp.DTO.SkuDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Sku {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false)
    private double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    public SkuDTO toSkuDTO(){
        return new SkuDTO(id,code,color,price);
    }
    @Override
    public String toString() {
        return "Sku{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", color='" + color + '\'' +
                ", price=" + price +
                ", product=" + product +
                '}';
    }
}
