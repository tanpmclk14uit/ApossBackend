package com.example.apossbackend.service;

import com.example.apossbackend.model.dto.SignUpDTO;
import com.example.apossbackend.model.entity.CustomerEntity;
import org.springframework.security.core.Authentication;

public interface AuthService {

    boolean isEmailExist(String email);
    CustomerEntity createCustomer(SignUpDTO signUpDTO);
    Authentication signIn(String email, String password);
}
