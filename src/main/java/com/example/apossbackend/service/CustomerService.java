package com.example.apossbackend.service;

import com.example.apossbackend.model.dto.CustomerDTO;
import com.example.apossbackend.model.dto.SignUpDTO;

import java.util.List;

public interface CustomerService {
    CustomerDTO findUserInformationByEmail(String email, String token);
}
