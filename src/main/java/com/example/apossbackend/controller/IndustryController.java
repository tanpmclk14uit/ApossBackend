package com.example.apossbackend.controller;

import com.example.apossbackend.model.dto.DetailCategoryDTO;
import com.example.apossbackend.service.IndustryService;
import com.example.apossbackend.service.impl.IndustryServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/industry")
public class IndustryController {

    private final IndustryService industryService;

    public  IndustryController (IndustryService industryService){
        this.industryService = industryService;
    }

    @GetMapping("/detail_category")
    public List<DetailCategoryDTO> getAllDetailCategory(){
        return industryService.getAllDetailCategory();
    }
}
