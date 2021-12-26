package com.example.apossbackend.service;


import com.example.apossbackend.model.entity.ConfirmationToken;
import com.example.apossbackend.model.entity.CustomerEntity;

public interface ConfirmationService {
    ConfirmationToken findByToken(String token);
    String createNewToken(CustomerEntity customer);
}
