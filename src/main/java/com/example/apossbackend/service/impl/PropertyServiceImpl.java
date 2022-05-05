package com.example.apossbackend.service.impl;

import com.example.apossbackend.exception.ResourceNotFoundException;
import com.example.apossbackend.model.dto.PropertyDTO;
import com.example.apossbackend.model.dto.PropertyValueDTO;
import com.example.apossbackend.model.entity.ClassifyProductEntity;
import com.example.apossbackend.model.entity.ClassifyProductValueEntity;
import com.example.apossbackend.repository.PropertyRepository;
import com.example.apossbackend.repository.PropertyValueRepository;
import com.example.apossbackend.repository.SetValueRepository;
import com.example.apossbackend.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyValueRepository propertyValueRepository;
    private final SetValueRepository setValueRepository;

    @Autowired
    public PropertyServiceImpl(PropertyRepository propertyRepository, PropertyValueRepository propertyValueRepository, SetValueRepository setValueRepository) {
        this.propertyRepository = propertyRepository;
        this.propertyValueRepository = propertyValueRepository;
        this.setValueRepository = setValueRepository;
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
        try {
            ClassifyProductEntity newClassifyProductEntity = convertPropertyDTOToClassifyProductEntity(newProperty);
            return propertyRepository.saveAndFlush(newClassifyProductEntity).getId();
        } catch (Exception e) {
            return -1L;
        }
    }

    @Override
    public Long addNewPropertyValueForProperty(PropertyValueDTO newPropertyValue, Long propertyId) {
        ClassifyProductEntity classifyProductEntity = propertyRepository.findById(propertyId).orElseThrow(
                () -> new ResourceNotFoundException("property", "id", propertyId)
        );
        try {
            ClassifyProductValueEntity classifyProductValueEntity = convertPropertyValueDTOtoClassifyProductValueEntity(newPropertyValue, classifyProductEntity);
            return propertyValueRepository.saveAndFlush(classifyProductValueEntity).getId();
        } catch (Exception e) {
            return -1L;
        }
    }

    @Override
    public void updatePropertyNameByPropertyId(String newName, Long propertyId) {
        ClassifyProductEntity classifyProductEntity = propertyRepository.findById(propertyId).orElseThrow(
                () -> new ResourceNotFoundException("property", "id", propertyId)
        );
        classifyProductEntity.setName(newName);
        classifyProductEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        propertyRepository.save(classifyProductEntity);
    }

    @Override
    public void updatePropertyValueByPropertyValueId(PropertyValueDTO propertyValueDTO, Long propertyValueId) {
        ClassifyProductValueEntity classifyProductValueEntity = propertyValueRepository.findById(propertyValueId).orElseThrow(
                () -> new ResourceNotFoundException("property value", "id", propertyValueId)
        );
        classifyProductValueEntity.setValue(propertyValueDTO.getValue());
        classifyProductValueEntity.setName(propertyValueDTO.getName());
        classifyProductValueEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        propertyValueRepository.save(classifyProductValueEntity);
    }

    @Override
    @Transactional
    public Boolean deletePropertyByIdSuccess(Long propertyId) {
        if (setValueRepository.countSetValueEntitiesByClassifyProductId(propertyId) == 0) {
            propertyValueRepository.deleteAllByClassifyProductId(propertyId);
            propertyRepository.deleteById(propertyId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean deletePropertyValueByIdSuccess(long propertyValueId) {
        if (!setValueRepository.existsSetValueEntityByClassifyProductValueId(propertyValueId)) {
            propertyValueRepository.deleteById(propertyValueId);
            return true;
        } else {
            return false;
        }
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

    private ClassifyProductEntity convertPropertyDTOToClassifyProductEntity(PropertyDTO propertyDTO) {
        ClassifyProductEntity classifyProductEntity = new ClassifyProductEntity();
        classifyProductEntity.setName(propertyDTO.getName());
        classifyProductEntity.setPropertyColor(propertyDTO.isColor());
        classifyProductEntity.setCreateTime(new Timestamp(new Date().getTime()));
        classifyProductEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        return classifyProductEntity;
    }

    private ClassifyProductValueEntity convertPropertyValueDTOtoClassifyProductValueEntity(PropertyValueDTO propertyValueDTO, ClassifyProductEntity classifyProductEntity) {
        ClassifyProductValueEntity classifyProductValueEntity = new ClassifyProductValueEntity();
        classifyProductValueEntity.setClassifyProduct(classifyProductEntity);
        classifyProductValueEntity.setValue(propertyValueDTO.getValue());
        classifyProductValueEntity.setName(propertyValueDTO.getName());
        classifyProductValueEntity.setCreateTime(new Timestamp(new Date().getTime()));
        classifyProductValueEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        return classifyProductValueEntity;
    }
}
