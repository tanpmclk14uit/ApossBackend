package com.example.apossbackend.service.impl;

import com.example.apossbackend.exception.ResourceNotFoundException;
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

    @Override
    public void addIndustry(DetailCategoryDTO detailCategoryDTO) {
            industryRepository.save(convertToIndustryEntity(detailCategoryDTO));
    }

    @Override
    public void deleteIndustry(long id) {
        if (industryRepository.existsIndustryEntityById(id)) {
            industryRepository.deleteById(id);
        } else {
            throw  new ResourceNotFoundException("Indutry", "id", id);
        }
    }

    @Override
    public void updateIndustry(DetailCategoryDTO newDetailCategoryDTO, long id) {
        IndustryEntity industryEntity = industryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Industry", "id", id)
        );
        industryEntity.setName(newDetailCategoryDTO.getName());
        industryEntity.setIndustryImages(convertToIndustryImageEntities(newDetailCategoryDTO.getImages(), industryEntity));
        industryRepository.save(industryEntity);
    }

    private IndustryEntity convertToIndustryEntity(DetailCategoryDTO detailCategoryDTO){
        IndustryEntity industryEntity = new IndustryEntity();
        industryEntity.setName(detailCategoryDTO.getName());
        industryEntity.setIndustryImages(convertToIndustryImageEntities(detailCategoryDTO.getImages(), industryEntity));
        return industryEntity;
    }

    private  List<IndustryImageEntity> convertToIndustryImageEntities(List<String> images, IndustryEntity industry){
        ArrayList<IndustryImageEntity> industryImageEntities = new ArrayList<>();
        int position = 1;
        for (String image : images) {
            industryImageEntities.add(new IndustryImageEntity(
                    industry,
                    position,
                    image
            ));
            position++;
        }
        return industryImageEntities;
    }
}
