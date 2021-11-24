package com.example.apossbackend.service.impl;

import com.example.apossbackend.exception.ApossBackendException;
import com.example.apossbackend.model.dto.OrderDTO;
import com.example.apossbackend.model.dto.OrderItemDTO;
import com.example.apossbackend.model.entity.CustomerEntity;
import com.example.apossbackend.model.entity.OrderEntity;
import com.example.apossbackend.model.entity.OrderItemEntity;
import com.example.apossbackend.repository.*;
import com.example.apossbackend.security.JwtTokenProvider;
import com.example.apossbackend.service.OrderService;
import com.example.apossbackend.utils.enums.OrderStatus;
import io.jsonwebtoken.Jwt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final ProvinceRepository provinceRepository;
    private final DistrictRepository districtRepository;
    private final WardRepository wardRepository;


    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper, JwtTokenProvider jwtTokenProvider, CustomerRepository customerRepository,
                            ProvinceRepository provinceRepository, DistrictRepository districtRepository, WardRepository wardRepository) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.customerRepository = customerRepository;
        this.wardRepository = wardRepository;
        this.districtRepository = districtRepository;
        this.provinceRepository = provinceRepository;
    }

    @Override
    public List<OrderDTO> findAllOrderByCustomerIdAndStatus(OrderStatus status, String accessToken) {
        String userName = jwtTokenProvider.getUsernameFromJWT(accessToken);
        List<OrderEntity> orderEntity = orderRepository.findOrderEntitiesByCustomerEmailAndStatus(userName, status);
        return orderEntity.stream().map(this::mapToOrderDTO).collect(Collectors.toList());
    }

    @Override
    public void addNewOrder(String accessToken, OrderDTO orderDTO) {
        String email = jwtTokenProvider.getUsernameFromJWT(accessToken);
        Optional<CustomerEntity> customerOptional = customerRepository.findByEmail(email);
        if (customerOptional.isPresent())
        {
            CustomerEntity customer = customerOptional.get();
            OrderEntity orderEntity = mapToOrderEntity(orderDTO, customer);
            orderRepository.save(orderEntity);
        }
        else
        {
            throw new ApossBackendException(HttpStatus.BAD_REQUEST, "You don't have permission to do this action!");
        }
    }

    private OrderItemDTO mapToOrderItemDTO(OrderItemEntity orderItemEntity){
        return modelMapper.map(orderItemEntity, OrderItemDTO.class);
    }

    private  OrderItemEntity mapToOrderItemEntity(OrderItemDTO orderItemDTO){
        return modelMapper.map(orderItemDTO, OrderItemEntity.class);
    }
    private OrderDTO mapToOrderDTO(OrderEntity orderEntity){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(orderEntity.getId());
        orderDTO.setOrderTime(orderEntity.getCreateTime());
        orderDTO.setOrderStatus(orderEntity.getStatus());
        orderDTO.setAddress(orderEntity.getDeliveryAddress());
        orderDTO.setTotalPrice(orderEntity.getTotalPrice());
        orderDTO.setCancelReason(orderEntity.getCancelReason());
        List<OrderItemDTO> orderItemDTOList = orderEntity.getItems().stream().map(this::mapToOrderItemDTO).collect(Collectors.toList());
        orderDTO.setOrderItemDTOList(orderItemDTOList.subList(0, 1));
        return orderDTO;
    }

    protected OrderEntity mapToOrderEntity(OrderDTO orderDTO, CustomerEntity customerEntity)
    {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setCustomer(customerEntity);
        orderEntity.setItems(orderDTO.getOrderItemDTOList().stream().map(this::mapToOrderItemEntity).collect(Collectors.toList()));
        orderEntity.setDeliveryAddress(orderDTO.getAddress());
        orderEntity.setTotalPrice(orderDTO.getTotalPrice());
        orderEntity.setStatus(OrderStatus.Pending);
        String[] listAddressName= orderDTO.getAddress().split(", ");
        long province = provinceRepository.getAllByName(listAddressName[listAddressName.length-1]).getId();
        long district = districtRepository.getDistrictEntityByNameAndProvince_Id(listAddressName[listAddressName.length-2], province).getId();
        long ward = wardRepository.getWardEntityByNameAndDistrict_Id(listAddressName[listAddressName.length-3], district).getId();
        orderEntity.setProvince(province);
        orderEntity.setDistrict(district);
        orderEntity.setWard(ward);
        return orderEntity;
    }
}
