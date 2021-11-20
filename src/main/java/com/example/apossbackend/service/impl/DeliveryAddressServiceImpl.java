package com.example.apossbackend.service.impl;

import com.example.apossbackend.exception.ApossBackendException;
import com.example.apossbackend.exception.ResourceNotFoundException;
import com.example.apossbackend.model.dto.DeliveryAddressDTO;
import com.example.apossbackend.model.dto.DistrictDTO;
import com.example.apossbackend.model.dto.ProvinceDTO;
import com.example.apossbackend.model.dto.WardDTO;
import com.example.apossbackend.model.entity.*;
import com.example.apossbackend.repository.*;
import com.example.apossbackend.security.JwtTokenProvider;
import com.example.apossbackend.service.DeliveryAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DeliveryAddressServiceImpl implements DeliveryAddressService {

    private final DeliveryAddressRepository deliveryAddressRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;
    private final ProvinceRepository provinceRepository;
    private final CustomerRepository customerRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public DeliveryAddressServiceImpl(DeliveryAddressRepository deliveryAddressRepository, JwtTokenProvider jwtTokenProvider, DistrictRepository districtRepository,
                                      WardRepository wardRepository, ProvinceRepository provinceRepository, CustomerRepository customerRepository)
    {
        this.deliveryAddressRepository = deliveryAddressRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customerRepository = customerRepository;
        this.wardRepository = wardRepository;
        this.districtRepository = districtRepository;
        this.provinceRepository = provinceRepository;
    }

    @Override
    public List<DeliveryAddressDTO> getAllDeliveryByCustomer(String accessToken) {
        String email = jwtTokenProvider.getUsernameFromJWT(accessToken);
        List<DeliveryAddressEntity> listDeliveryAddressEntity = deliveryAddressRepository.findAllByCustomer_Email(email);
        return listDeliveryAddressEntity.stream().map(this::convertDeliveryAddressFromEntityToDTO).collect(Collectors.toList());
    }

    @Override
    public void addNewDeliveryAddress(String accessToken, DeliveryAddressDTO deliveryAddressDTO) {
        String email = jwtTokenProvider.getUsernameFromJWT(accessToken);
        deliveryAddressRepository.save(convertDeliveryAddressDTOToEntity(deliveryAddressDTO, email));
    }

    @Override
    public void deleteDeliveryAddress(String accessToken, Long deliveryAddressId) {
        String email = jwtTokenProvider.getUsernameFromJWT(accessToken);
        DeliveryAddressEntity deliveryAddressEntity = deliveryAddressRepository.findDeliveryAddressEntityById(deliveryAddressId).orElseThrow(
                () -> new ApossBackendException("Error", HttpStatus.BAD_REQUEST, "Not found delivery address")
        );
        if (Objects.equals(deliveryAddressEntity.getCustomer().getEmail(), email))
        {
            deliveryAddressRepository.delete(deliveryAddressEntity);
        }
        else {
            throw new ApossBackendException("Error", HttpStatus.BAD_REQUEST, "You don't have permission to do this action!");
        }
    }

    @Override
    public void updateDeliveryAddress(String accessToken, DeliveryAddressDTO deliveryAddressDTO) {
        String email = jwtTokenProvider.getUsernameFromJWT(accessToken);
        DeliveryAddressEntity deliveryAddressEntity = deliveryAddressRepository.findDeliveryAddressEntityById(deliveryAddressDTO.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Cart", "id", deliveryAddressDTO.getId())
        );
        if (Objects.equals(deliveryAddressEntity.getCustomer().getEmail(), email))
        {
            deliveryAddressEntity.setAddressLane(deliveryAddressDTO.getAddressLane());
            deliveryAddressEntity.setGender(deliveryAddressDTO.getGender());
            deliveryAddressEntity.setPhoneNumber(deliveryAddressDTO.getPhoneNumber());
            deliveryAddressEntity.setReceiverName(deliveryAddressDTO.getName());
            deliveryAddressEntity.setUpdateTime(new Timestamp(new Date().getTime()));
            deliveryAddressEntity.setIsDefault(deliveryAddressDTO.getIsDefault());
            deliveryAddressEntity.setDistrict(convertDistrictDTOToEntity(deliveryAddressDTO.getDistrict()));
            deliveryAddressEntity.setProvince(convertProvinceDTOToEntity(deliveryAddressDTO.getProvince()));
            deliveryAddressEntity.setWard(convertWardDTOToEntity(deliveryAddressDTO.getWard()));
            deliveryAddressRepository.save(deliveryAddressEntity);
        }
        else {
            throw new ApossBackendException(HttpStatus.BAD_REQUEST, "You don't have permission to do this action!");
        }
    }

    private DeliveryAddressEntity convertDeliveryAddressDTOToEntity(DeliveryAddressDTO deliveryAddressDTO, String email)
    {
        DeliveryAddressEntity deliveryAddressEntity = new DeliveryAddressEntity();
        CustomerEntity customerEntity = customerRepository.findByEmail(email).orElseThrow(
                () -> new ApossBackendException("Error", HttpStatus.BAD_REQUEST, "Not found customer")
        );
        deliveryAddressEntity.setCustomer(customerEntity);
        deliveryAddressEntity.setAddressLane(deliveryAddressDTO.getAddressLane());
        deliveryAddressEntity.setGender(deliveryAddressDTO.getGender());
        deliveryAddressEntity.setPhoneNumber(deliveryAddressDTO.getPhoneNumber());
        deliveryAddressEntity.setReceiverName(deliveryAddressDTO.getName());
        deliveryAddressEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        deliveryAddressEntity.setCreateTime(new Timestamp(new Date().getTime()));
        deliveryAddressEntity.setIsDefault(deliveryAddressDTO.getIsDefault());
        deliveryAddressEntity.setDistrict(convertDistrictDTOToEntity(deliveryAddressDTO.getDistrict()));
        deliveryAddressEntity.setProvince(convertProvinceDTOToEntity(deliveryAddressDTO.getProvince()));
        deliveryAddressEntity.setWard(convertWardDTOToEntity(deliveryAddressDTO.getWard()));
        return deliveryAddressEntity;
    }

    public DeliveryAddressDTO convertDeliveryAddressFromEntityToDTO(DeliveryAddressEntity deliveryAddressEntity)
    {
        return new DeliveryAddressDTO(deliveryAddressEntity.getId(), deliveryAddressEntity.getReceiverName(),
                deliveryAddressEntity.getGender(), deliveryAddressEntity.getPhoneNumber(), convertProvinceEntityToDTO(deliveryAddressEntity.getProvince()),
                convertDistrictEntityToDTO(deliveryAddressEntity.getDistrict()), convertWardEntityToDTO(deliveryAddressEntity.getWard()),
                deliveryAddressEntity.getAddressLane(), deliveryAddressEntity.getIsDefault());
    }

    private DistrictDTO convertDistrictEntityToDTO(DistrictEntity districtEntity)
    {
        return new DistrictDTO(districtEntity.getId(), districtEntity.getName(), districtEntity.getProvince().getId());
    }

    private DistrictEntity convertDistrictDTOToEntity(DistrictDTO districtDTO)
    {
        return districtRepository.getById(districtDTO.getId());
    }

    private WardEntity convertWardDTOToEntity(WardDTO wardDTO)
    {
        return wardRepository.getById(wardDTO.getId());
    }

    private ProvinceEntity convertProvinceDTOToEntity(ProvinceDTO provinceDTO)
    {
        return provinceRepository.getById(provinceDTO.getId());
    }


    private ProvinceDTO convertProvinceEntityToDTO(ProvinceEntity provinceEntity)
    {
        return new ProvinceDTO(provinceEntity.getId(), provinceEntity.getName());
    }

    public WardDTO convertWardEntityToDTO(WardEntity wardEntity)
    {
        return new WardDTO(wardEntity.getId(), wardEntity.getName(), wardEntity.getDistrict().getId());
    }
}
