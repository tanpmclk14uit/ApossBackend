package com.example.apossbackend.controller;

import com.example.apossbackend.model.dto.DistrictDTO;
import com.example.apossbackend.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping()
    public List<DistrictDTO> getDistrictById(@RequestParam(value = "id") Long id)
    {
        return districtService.getDistrictById(id);
    }
}
