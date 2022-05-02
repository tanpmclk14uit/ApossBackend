package com.example.apossbackend.service.impl;

import com.example.apossbackend.exception.ResourceNotFoundException;
import com.example.apossbackend.model.dto.PropertyDTO;
import com.example.apossbackend.model.dto.PropertyValueDTO;
import com.example.apossbackend.model.entity.ClassifyProductEntity;
import com.example.apossbackend.model.entity.ClassifyProductValueEntity;
import com.example.apossbackend.repository.PropertyRepository;
import com.example.apossbackend.repository.PropertyValueRepository;
import com.example.apossbackend.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyValueRepository propertyValueRepository;

    @Autowired
    public PropertyServiceImpl(PropertyRepository propertyRepository, PropertyValueRepository propertyValueRepository) {
        this.propertyRepository = propertyRepository;
        this.propertyValueRepository = propertyValueRepository;
    }

    @Override
    public List<PropertyDTO> getAllProperties() {
        List<ClassifyProductEntity> classifyProductEntities = propertyRepository.findAll();
        classifyProductEntities.remove(0);
        return classifyProductEntities.stream().map(this::convertClassifyProductEntityToPropertyDTO).collect(Collectors.toList());
    }

    @Override
    public List<PropertyValueDTO> getAllPropertyValuesByPropertyId(Long propertyId) {
        if (propertyId != 0) {
            propertyRepository.findById(propertyId).orElseThrow(
                    () -> new ResourceNotFoundException("Property", "Id", propertyId)
            );
            List<ClassifyProductValueEntity> classifyProductValueEntities = propertyValueRepository.findClassifyProductValueEntitiesByClassifyProductId(propertyId);
            return classifyProductValueEntities.stream().map(this::convertClassifyProductEntityValueToPropertyValueDTO).collect(Collectors.toList());
        } else {
            throw new ResourceNotFoundException("Property", "Id", propertyId);
        }
    }

    @Override
    public Long addNewProperty(PropertyDTO newProperty) {
        return null;
    }

    @Override
    public Long addNewPropertyValueForProperty(PropertyValueDTO newPropertyValue, Long propertyId) {
        return null;
    }

    @Override
    public void updatePropertyNameByPropertyId(String newName, Long propertyId) {

    }

    @Override
    public void updatePropertyValueByPropertyValueId(Long propertyValueId) {

    }

    private PropertyDTO convertClassifyProductEntityToPropertyDTO(ClassifyProductEntity classifyProductEntity) {
        PropertyDTO propertyDTO = new PropertyDTO();
        propertyDTO.setColor(classifyProductEntity.isPropertyColor());
        propertyDTO.setId(classifyProductEntity.getId());
        propertyDTO.setName(classifyProductEntity.getName());
        return propertyDTO;
    }

    private PropertyValueDTO convertClassifyProductEntityValueToPropertyValueDTO(ClassifyProductValueEntity classifyProductValueEntity) {
        PropertyValueDTO propertyValueDTO = new PropertyValueDTO();
        propertyValueDTO.setId(classifyProductValueEntity.getId());
        propertyValueDTO.setName(classifyProductValueEntity.getName());
        propertyValueDTO.setValue(classifyProductValueEntity.getValue());
        return propertyValueDTO;
    }
}
