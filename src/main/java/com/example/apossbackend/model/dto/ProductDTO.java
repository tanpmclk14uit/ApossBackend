package com.example.apossbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private long id;
    private String imageUrl;
    private String name;
    private int price;
    private float rating;
    private boolean isFavorite;
}

