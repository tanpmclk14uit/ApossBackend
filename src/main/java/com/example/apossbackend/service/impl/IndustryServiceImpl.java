package com.example.apossbackend.service.impl;

import com.example.apossbackend.model.dto.DetailCategoryDTO;
import com.example.apossbackend.model.entity.IndustryEntity;
import com.example.apossbackend.model.entity.IndustryImageEntity;
import com.example.apossbackend.repository.IndustryRepository;
import com.example.apossbackend.service.IndustryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndustryServiceImpl implements IndustryService {

    private final IndustryRepository industryRepository;

    public IndustryServiceImpl(IndustryRepository industryRepository){
        this.industryRepository = industryRepository;
    }

    @Override
    public List<DetailCategoryDTO> getAllDetailCategory() {
        List<IndustryEntity> industryEntityList = industryRepository.findAll();
        List<DetailCategoryDTO> detailCategoryDTOList = new ArrayList<DetailCategoryDTO>();
        for (IndustryEntity item: industryEntityList) {
            DetailCategoryDTO detailCategoryDTO = new DetailCategoryDTO();
            detailCategoryDTO.setId(item.getId());
            detailCategoryDTO.setName(item.getName());
            detailCategoryDTO.setImages(item.getIndustryImages().stream().map(IndustryImageEntity::getImageUrl).collect(Collectors.toList()));
            detailCategoryDTOList.add(detailCategoryDTO);
        }
        return detailCategoryDTOList;
    }
}
