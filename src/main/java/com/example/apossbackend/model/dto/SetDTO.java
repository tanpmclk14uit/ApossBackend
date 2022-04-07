package com.example.apossbackend.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class SetDTO {
    private long setId;
    private int additionalPrice;
    private int quantity;
    private List<Long> valueIds;
}
