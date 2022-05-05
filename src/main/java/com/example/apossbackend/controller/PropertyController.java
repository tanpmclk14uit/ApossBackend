package com.example.apossbackend.controller;

import com.example.apossbackend.model.dto.PropertyDTO;
import com.example.apossbackend.model.dto.PropertyValueDTO;
import com.example.apossbackend.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> addNewProperty(
            @RequestBody PropertyDTO propertyDTO
    ) {
        return ResponseEntity.ok(propertyService.addNewProperty(propertyDTO));
    }

    @PostMapping("/{id}/value")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> addNewPropertyValueForProperty(
            @RequestBody PropertyValueDTO newPropertyValue,
            @PathVariable("id") Long propertyId) {
        return ResponseEntity.ok(propertyService.addNewPropertyValueForProperty(newPropertyValue, propertyId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updatePropertyNameByPropertyId(
            @RequestBody String newName,
            @PathVariable("id") Long propertyId) {
        propertyService.updatePropertyNameByPropertyId(newName, propertyId);
        return ResponseEntity.ok("\"Update property success\"");
    }

    @PutMapping("/value/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updatePropertyValueByPropertyValueId(
            @RequestBody PropertyValueDTO propertyValueDTO,
            @PathVariable("id") Long propertyValueId) {
        propertyService.updatePropertyValueByPropertyValueId(propertyValueDTO, propertyValueId);
        return ResponseEntity.ok("\"Update property value success\"");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePropertyById(
            @PathVariable("id") Long propertyId) {
        if (propertyService.deletePropertyByIdSuccess(propertyId)) {
            return ResponseEntity.ok("\"Delete property success\"");
        } else {
            return new ResponseEntity<String>("\"Can't delete property applied for product\"", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/value/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePropertyValueById(
            @PathVariable("id") long propertyValueId) {
        if (propertyService.deletePropertyValueByIdSuccess(propertyValueId)) {
            return ResponseEntity.ok("\"Delete property value success\"");
        } else {
            return new ResponseEntity<String>("\"Can't delete property value applied for product\"", HttpStatus.BAD_REQUEST);
        }
    }
}
