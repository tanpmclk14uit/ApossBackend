package com.example.apossbackend.controller;

import com.example.apossbackend.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/district")
public class DistrictController {

    private  final DistrictService districtService;

    @Autowired
    public DistrictController(DistrictService districtService)
    {
        this.districtService = districtService;
    }
}
