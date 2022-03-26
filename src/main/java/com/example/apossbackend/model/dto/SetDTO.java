package com.example.apossbackend.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class SetDTO {
    private long setId;
    private long productId;
    private int additionalPrice;
    private int quantity;
    private List<SetValueDTO> setValueDTOList;
}
