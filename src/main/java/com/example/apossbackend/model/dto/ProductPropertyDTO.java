package com.example.apossbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPropertyDTO {
    private long id;
    private String name;
    private boolean isColor;
    private List<ProductPropertyValueDTO> valueDTOS;

    public ProductPropertyDTO(long id, String name, boolean isColor){
        this.id = id;
        this.name = name;
        this.isColor = isColor;
        this.valueDTOS = new ArrayList<>();
    }
}
