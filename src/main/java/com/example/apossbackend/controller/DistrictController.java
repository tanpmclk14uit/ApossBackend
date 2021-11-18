package com.example.apossbackend.controller;

import com.example.apossbackend.model.dto.DistrictDTO;
import com.example.apossbackend.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/district")
public class DistrictController {

    private  final DistrictService districtService;

    @Autowired
    public DistrictController(DistrictService districtService)
    {
        this.districtService = districtService;
    }

    @GetMapping()
    public List<DistrictDTO> getDistrictById(@RequestParam(value = "id") Long id)
    {
        return districtService.getDistrictById(id);
    }
}
