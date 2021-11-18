package com.example.apossbackend.controller;

import com.example.apossbackend.model.dto.WardDTO;
import com.example.apossbackend.service.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/ward")
public class WardController {

    private  final WardService wardService;

    @Autowired
    public WardController(WardService wardService)
    {
        this.wardService = wardService;
    }

    @GetMapping()
    public List<WardDTO> getAllWardById(@RequestParam(value = "id") Long id)
    {
        return wardService.getAllWardById(id);
    }
}
