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
    public CustomerDTO findUserInformationByToken(String token) {
        if(StringUtils.hasText(token) && tokenProvider.validateToken(token)){
            String username = tokenProvider.getUsernameFromJWT(token);
            CustomerEntity customer = customerRepository.findByEmail(username).orElseThrow(
                    () ->new ApossBackendException(HttpStatus.BAD_REQUEST, "User not found"));
            return  mapToCustomerDTO(customer);
        }else{
            throw new ApossBackendException(HttpStatus.BAD_REQUEST, "User not found");
        }
    }

    @Override
    public CustomerDTO findUserInformationById(long id) {
        CustomerEntity customer = customerRepository.findById(id).orElseThrow(
                () ->new ResourceNotFoundException("Customer", "id", id));
        return mapToCustomerDTO(customer);
    }
    private CustomerDTO mapToCustomerDTO(CustomerEntity customerEntity) {
        return modelMapper.map(customerEntity, CustomerDTO.class);
    }

}
