package com.example.apossbackend.service.impl;

import com.example.apossbackend.model.dto.DistrictDTO;
import com.example.apossbackend.model.entity.DistrictEntity;
import com.example.apossbackend.repository.DistrictRepository;
import com.example.apossbackend.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DistrictServiceImpl implements DistrictService {

    private final DistrictRepository districtRepository;

    @Autowired
    public DistrictServiceImpl(DistrictRepository districtRepository)
    {
        this.districtRepository = districtRepository;
    }


    @Override
    public List<DistrictDTO> getDistrictById(Long id) {
        List<DistrictEntity> lstDistrictEntity = districtRepository.getDistrictEntityByProvince_Id(id);
        return lstDistrictEntity.stream().map(this::convertDistrictEntityToDTO).collect(Collectors.toList());
    }

    private DistrictDTO convertDistrictEntityToDTO(DistrictEntity districtEntity)
    {
        return new DistrictDTO(districtEntity.getId(), districtEntity.getName(), districtEntity.getProvince().getId());
    }
}
