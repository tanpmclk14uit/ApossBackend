package com.example.apossbackend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddressDTO {
    private Long id;
    private String name;
    private Boolean gender;
    private String phoneNumber;
    private ProvinceDTO province;
    private DistrictDTO district;
    private WardDTO ward;
    private String addressLane;
    private Boolean isDefault;
}
