package com.example.apossbackend.service;


import com.example.apossbackend.model.entity.ConfirmationToken;
import com.example.apossbackend.model.entity.CustomerEntity;

public interface ConfirmationService {
    public ConfirmationToken findByToken(String token);
    public void createNewToken(CustomerEntity customer);
}
