package com.example.apossbackend.service;

import com.example.apossbackend.model.dto.DeliveryAddressDTO;

import java.util.List;

public interface DeliveryAddressService {
    List<DeliveryAddressDTO> getAllDeliveryByCustomer(String request);

    void addNewDeliveryAddress(String accessToken, DeliveryAddressDTO deliveryAddressDTO);

    void deleteDeliveryAddress(String accessToken, Long deliveryAddressId);

    void updateDeliveryAddress(String accessToken, DeliveryAddressDTO deliveryAddressDTO);

    DeliveryAddressDTO getCurrentDefaultDeliveryAddressByCustomer(String accessToken);
}
