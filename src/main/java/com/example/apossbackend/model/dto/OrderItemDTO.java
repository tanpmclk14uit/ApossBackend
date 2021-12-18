package com.example.apossbackend.model.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private long id;
    private  long product;
    private String imageUrl;
    private String name;
    private int price;
    private int amount;
    private String property;
}
