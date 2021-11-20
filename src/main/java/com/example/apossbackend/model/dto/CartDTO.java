package com.example.apossbackend.model.dto;

import lombok.Data;

@Data
public class CartDTO {
    private long id;
    private String property;
    private int quantity;
    private long productId;
    private String imageUrl;
    private String name;
    private int price;
}
