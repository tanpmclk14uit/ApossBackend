package com.example.apossbackend.service.impl;

import com.example.apossbackend.repository.DistrictRepository;
import com.example.apossbackend.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DistrictServiceImpl implements DistrictService {

    private final DistrictRepository districtRepository;

    @Autowired
    public DistrictServiceImpl(DistrictRepository districtRepository)
    {
        this.districtRepository = districtRepository;
    }
}
