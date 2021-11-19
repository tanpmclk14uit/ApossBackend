package com.example.apossbackend.model.dto;

import lombok.Data;

@Data
public class ProductDetailDTO {
    private String name;
    private int price;
    private int purchase;
    private float rating;
    private String description;
    private int quantity;
    private String kindName;
    private long kindId;
    private int totalReview;
}
