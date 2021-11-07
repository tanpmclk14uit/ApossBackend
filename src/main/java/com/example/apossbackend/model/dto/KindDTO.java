package com.example.apossbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KindDTO {
    private Long id;
    private String name;
    private int totalPurchases;
    private int totalProducts;
    private float rating;
    private String image;
    private List<ProductDTO> products;
    private long category;
}
