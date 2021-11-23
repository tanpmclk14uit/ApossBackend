package com.example.apossbackend.model.dto;

import lombok.Data;

@Data
public class ProductPropertyValueDTO {
    private long id;
    private String name;
    private String value;
    private int quantity;
    private int additionalPrice;
}
