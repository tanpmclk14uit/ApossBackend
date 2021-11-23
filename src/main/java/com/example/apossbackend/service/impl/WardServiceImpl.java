package com.example.apossbackend.service.impl;

import com.example.apossbackend.model.dto.WardDTO;
import com.example.apossbackend.model.entity.WardEntity;
import com.example.apossbackend.repository.WardRepository;
import com.example.apossbackend.service.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WardServiceImpl implements WardService {

    private final WardRepository wardRepository;

    @Autowired
    public WardServiceImpl(WardRepository wardRepository)
    {
        this.wardRepository = wardRepository;
    }

    @Override
    public List<WardDTO> getAllWardById(Long id) {
        List<WardEntity> listWardEntity = wardRepository.getAllByDistrict_Id(id);
        return listWardEntity.stream().map(this::convertWardEntityToDTO).collect(Collectors.toList());
    }

    public WardDTO convertWardEntityToDTO(WardEntity wardEntity)
    {
        return new WardDTO(wardEntity.getId(), wardEntity.getName(), wardEntity.getDistrict().getId());
    }
}
