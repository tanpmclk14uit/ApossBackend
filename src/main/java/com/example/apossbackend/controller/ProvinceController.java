package com.example.apossbackend.controller;

import com.example.apossbackend.model.dto.ProvinceDTO;
import com.example.apossbackend.model.entity.ProductEntity;
import com.example.apossbackend.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/province")
public class ProvinceController {

    private final ProvinceService provinceService;

    @Autowired
    public ProvinceController(ProvinceService provinceService)
    {
        this.provinceService = provinceService;
    }

    @GetMapping()
    public List<ProvinceDTO> getAllProvince()
    {
        return provinceService.getAllProvince();
    }
}
