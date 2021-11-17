package com.example.apossbackend.service.impl;

import com.example.apossbackend.repository.ProvinceRepository;
import com.example.apossbackend.service.ProvinceService;
import org.springframework.stereotype.Service;

@Service
public class ProvinceServiceImpl implements ProvinceService {

    private final ProvinceRepository provinceRepository;

    public ProvinceServiceImpl(ProvinceRepository provinceRepository)
    {
        this.provinceRepository = provinceRepository;
    }
}
