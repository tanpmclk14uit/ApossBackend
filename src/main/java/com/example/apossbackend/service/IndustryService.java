package com.example.apossbackend.service;

import com.example.apossbackend.model.dto.DetailCategoryDTO;

import java.util.List;

public interface IndustryService {

    List<DetailCategoryDTO> getAllDetailCategory();
    void addIndustry(DetailCategoryDTO detailCategoryDTO);
    void deleteIndustry(long id);
    void updateIndustry(DetailCategoryDTO newDetailCategoryDTO, long id);
}
