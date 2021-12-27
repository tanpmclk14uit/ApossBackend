package com.example.apossbackend.service.impl;


import com.example.apossbackend.exception.ApossBackendException;
import com.example.apossbackend.exception.ResourceNotFoundException;
import com.example.apossbackend.model.dto.CustomerDTO;
import com.example.apossbackend.model.entity.CustomerEntity;
import com.example.apossbackend.repository.CustomerRepository;
import com.example.apossbackend.security.JwtTokenProvider;
import com.example.apossbackend.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final ModelMapper modelMapper;
    private final CustomerRepository customerRepository;
    private final JwtTokenProvider tokenProvider;

    public CustomerServiceImpl(CustomerRepository customerRepository, ModelMapper modelMapper, JwtTokenProvider tokenProvider) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public CustomerDTO findUserInformationByEmail(String email, String token) {
        String userName= tokenProvider.getUsernameFromJWT(token);
        CustomerEntity customer = customerRepository.findByEmail(userName).orElseThrow(
                ()-> new ResourceNotFoundException("Customer", "email", userName));
        if(customer.getEmail().equals(email)){
            return mapToCustomerDTO(customer);
        }else{
            throw new ApossBackendException(HttpStatus.BAD_REQUEST, "You don't have permission to do this action!");
        }
    }

    private CustomerDTO mapToCustomerDTO(CustomerEntity customerEntity) {
        return modelMapper.map(customerEntity, CustomerDTO.class);
    }

    @Override
    public CustomerEntity findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Customer account","email", email)
        );
    }
}
