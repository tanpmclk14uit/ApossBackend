package com.example.apossbackend.service;

import com.example.apossbackend.model.dto.PropertyDTO;
import com.example.apossbackend.model.dto.PropertyValueDTO;

import java.util.List;

public interface PropertyService {
    List<PropertyDTO> getAllProperties();

    List<PropertyValueDTO> getAllPropertyValuesByPropertyId(Long propertyId);

    Long addNewProperty(PropertyDTO newProperty);

    Long addNewPropertyValueForProperty(PropertyValueDTO newPropertyValue, Long propertyId);

    void updatePropertyNameByPropertyId(String newName, Long propertyId);

    void updatePropertyValueByPropertyValueId(PropertyValueDTO propertyValueDTO, Long propertyValueId);

    String deletePropertyById(Long propertyId);

    String deletePropertyValueById(long propertyValueId);

}
