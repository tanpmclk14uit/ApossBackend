package com.example.apossbackend.controller;

import com.example.apossbackend.service.WardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/ward")
public class WardController {

    private  final WardService wardService;

    @Autowired
    public WardController(WardService wardService)
    {
        this.wardService = wardService;
    }
}
