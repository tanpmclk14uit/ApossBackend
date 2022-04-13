package com.example.apossbackend.model.dto;

import lombok.Data;

@Data
public class OrderItemDTO {
    private long id;
    private  long product;
    private long cartId;
    private long setId;
    private String imageUrl;
    private String name;
    private int price;
    private int quantity;
    private String property;
}
