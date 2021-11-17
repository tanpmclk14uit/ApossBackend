package com.example.apossbackend.service.impl;

import com.example.apossbackend.repository.WardRepository;
import com.example.apossbackend.service.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WardServiceImpl implements WardService {

    private final WardRepository wardRepository;

    @Autowired
    public WardServiceImpl(WardRepository wardRepository)
    {
        this.wardRepository = wardRepository;
    }
}
