package com.example.apossbackend.model.dto;

import lombok.Data;

@Data
public class ProductImageDTO {
    private long id;
    private String imageUrl;
    private int priority;
}
