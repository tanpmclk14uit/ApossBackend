package com.example.apossbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductPropertyValueDTO {
    private long id;
    private String name;
    private String value;
}
