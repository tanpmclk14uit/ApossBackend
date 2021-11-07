package com.example.apossbackend.controller;

import com.example.apossbackend.service.IndustryImageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/industryImage")
public class IndustryImageController {

    private final IndustryImageService industryImageService;

    public IndustryImageController(IndustryImageService industryImageService){
        this.industryImageService = industryImageService;
    }

}
