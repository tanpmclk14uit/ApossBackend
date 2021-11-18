package com.example.apossbackend.service;

import com.example.apossbackend.model.dto.WardDTO;

import java.util.List;

public interface WardService {
    List<WardDTO> getAllWardById(Long id);
}
