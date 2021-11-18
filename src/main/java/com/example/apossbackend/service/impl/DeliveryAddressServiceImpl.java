package com.example.apossbackend.service.impl;

import com.example.apossbackend.repository.DeliveryAddressRepository;
import com.example.apossbackend.service.DeliveryAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryAddressServiceImpl implements DeliveryAddressService {

    private final DeliveryAddressRepository deliveryAddressRepository;

    @Autowired
    public DeliveryAddressServiceImpl(DeliveryAddressRepository deliveryAddressRepository)
    {
        this.deliveryAddressRepository = deliveryAddressRepository;
    }
}
