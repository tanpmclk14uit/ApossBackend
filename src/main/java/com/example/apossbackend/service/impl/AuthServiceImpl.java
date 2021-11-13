package com.example.apossbackend.service.impl;

import com.example.apossbackend.exception.ResourceNotFoundException;
import com.example.apossbackend.model.dto.SignUpDTO;
import com.example.apossbackend.model.entity.CustomerEntity;
import com.example.apossbackend.repository.CustomerRepository;
import com.example.apossbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImpl(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public boolean isEmailExist(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public void createCustomer(SignUpDTO signUpDTO) {
        CustomerEntity customer = new CustomerEntity();
        customer.setEmail(signUpDTO.getEmail());
        customer.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        customer.setImage(signUpDTO.getImageURL());
        customer.setBirthDay(signUpDTO.getBirthDate());
        customer.setName(signUpDTO.getName());
        customer.setPhoneNumber(signUpDTO.getPhoneNumber());
        customer.setGender(signUpDTO.getGender());
        customer.setCreateTime(new Timestamp(new Date().getTime()));
        customer.setUpdateTime(new Timestamp(new Date().getTime()));
        customerRepository.save(customer);
    }

    @Override
    public Authentication signIn(String email, String password) {
        if(!customerRepository.existsByEmail(email)){
            throw new ResourceNotFoundException("Account","Email",email);
        }
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                email, password));
    }
}
