package com.example.apossbackend.controller;

import com.example.apossbackend.model.dto.PropertyDTO;
import com.example.apossbackend.model.dto.PropertyValueDTO;
import com.example.apossbackend.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/property")
public class PropertyController {
    private final PropertyService propertyService;

    @Autowired
    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @GetMapping()
    public ResponseEntity<List<PropertyDTO>> getAllProperty() {
        return ResponseEntity.ok(propertyService.getAllProperties());
    }

    @GetMapping("/{id}/values")
    public ResponseEntity<List<PropertyValueDTO>> getAllPropertyValueByPropertyId(
            @PathVariable("id") long id
    ) {
        return ResponseEntity.ok(propertyService.getAllPropertyValuesByPropertyId(id));
    }


}
