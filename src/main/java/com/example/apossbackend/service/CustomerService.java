package com.example.apossbackend.service;

import com.example.apossbackend.model.dto.CustomerDTO;
import com.example.apossbackend.model.dto.SignUpDTO;
import com.example.apossbackend.model.entity.CustomerEntity;

import java.util.List;

public interface CustomerService {
    CustomerDTO findUserInformationByEmail(String email, String token);
    CustomerEntity findCustomerByEmail(String email);
}
