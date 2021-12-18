package com.example.apossbackend.service;

import com.example.apossbackend.model.dto.OrderDTO;
import com.example.apossbackend.model.dto.OrderItemDTO;
import com.example.apossbackend.utils.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    List<OrderDTO> findAllOrderByCustomerIdAndStatus(OrderStatus status, String accessToken);

    void addNewOrder(String accessToken, OrderDTO orderDTO);

    void holdOrder(String accessToken, List<OrderItemDTO> listOrderItemDTO);

    void reduceHold(String accessToken, List<OrderItemDTO> listOrderItemDTO);
}
