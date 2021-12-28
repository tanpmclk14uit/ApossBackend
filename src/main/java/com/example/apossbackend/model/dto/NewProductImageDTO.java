package com.example.apossbackend.model.dto;

import lombok.Data;

@Data
public class NewProductImageDTO {
    private String imageUrl;
    private int priority;
    private long productId;
}
