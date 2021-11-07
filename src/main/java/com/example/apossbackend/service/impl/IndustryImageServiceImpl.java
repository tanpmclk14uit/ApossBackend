package com.example.apossbackend.service.impl;

import com.example.apossbackend.model.entity.IndustryImageEntity;
import com.example.apossbackend.repository.IndustryImageRepository;
import com.example.apossbackend.service.IndustryImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Service
public class IndustryImageServiceImpl implements IndustryImageService {

    private final IndustryImageRepository industryImageRepository;

    public IndustryImageServiceImpl(IndustryImageRepository industryImageRepository){
        this.industryImageRepository = industryImageRepository;
    }

}
