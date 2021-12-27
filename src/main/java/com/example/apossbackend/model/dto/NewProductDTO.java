package com.example.apossbackend.model.dto;

import lombok.Data;

@Data
public class NewProductDTO {
    private String description;
    private String name;
    private int price;
    private int quantity;
    private long kindId;
}
