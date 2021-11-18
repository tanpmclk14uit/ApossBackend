package com.example.apossbackend.controller;

import com.example.apossbackend.model.dto.DeliveryAddressDTO;
import com.example.apossbackend.service.DeliveryAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/delivery_address")
public class DeliveryAddressController {

    private final DeliveryAddressService deliveryAddressService;

    @Autowired
    public DeliveryAddressController(DeliveryAddressService deliveryAddressService)
    {
        this.deliveryAddressService = deliveryAddressService;
    }

    public List<DeliveryAddressDTO> getAllDeliveryAddressByCustomer()
    {
        return null;
    }
}
