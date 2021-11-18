package com.example.apossbackend.service.impl;

import com.example.apossbackend.model.dto.ProvinceDTO;
import com.example.apossbackend.model.entity.ProvinceEntity;
import com.example.apossbackend.repository.ProvinceRepository;
import com.example.apossbackend.service.ProvinceService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProvinceServiceImpl implements ProvinceService {

    private final ProvinceRepository provinceRepository;

    public ProvinceServiceImpl(ProvinceRepository provinceRepository)
    {
        this.provinceRepository = provinceRepository;
    }

    @Override
    public List<ProvinceDTO> getAllProvince() {
         List<ProvinceEntity> lstProvinceEntity = provinceRepository.findAll();
        return lstProvinceEntity.stream().map(this::convertProvinceEntityToDTO).collect(Collectors.toList());
    }

    private ProvinceDTO convertProvinceEntityToDTO(ProvinceEntity provinceEntity)
    {
        return new ProvinceDTO(provinceEntity.getId(), provinceEntity.getName());
    }
}
