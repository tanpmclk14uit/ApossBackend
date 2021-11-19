package com.example.apossbackend.service.impl;

import com.example.apossbackend.model.dto.OrderDTO;
import com.example.apossbackend.model.dto.OrderItemDTO;
import com.example.apossbackend.model.entity.OrderEntity;
import com.example.apossbackend.model.entity.OrderItemEntity;
import com.example.apossbackend.repository.OrderRepository;
import com.example.apossbackend.security.JwtTokenProvider;
import com.example.apossbackend.service.OrderService;
import com.example.apossbackend.utils.enums.OrderStatus;
import io.jsonwebtoken.Jwt;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final JwtTokenProvider jwtTokenProvider;


    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ModelMapper modelMapper, JwtTokenProvider jwtTokenProvider) {
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public List<OrderDTO> findAllOrderByCustomerIdAndStatus(OrderStatus status, String accessToken) {
        String userName = jwtTokenProvider.getUsernameFromJWT(accessToken);
        List<OrderEntity> orderEntity = orderRepository.findOrderEntitiesByCustomerEmailAndStatus(userName, status);
        return orderEntity.stream().map(this::mapToOrderDTO).collect(Collectors.toList());
    }
    private OrderItemDTO mapToOrderItemDTO(OrderItemEntity orderItemEntity){
        return modelMapper.map(orderItemEntity, OrderItemDTO.class);
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
}
