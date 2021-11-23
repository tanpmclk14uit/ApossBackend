package com.example.apossbackend.service;

import com.example.apossbackend.model.dto.DistrictDTO;

import java.util.List;

public interface DistrictService {
    List<DistrictDTO> getDistrictById(Long id);
}
