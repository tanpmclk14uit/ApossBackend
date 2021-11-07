package com.example.apossbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailCategoryDTO {
    private long id;
    private String name;
    private int totalPurchases;
    private int totalProducts;
    private float rating;
    private List<String> images;
}
