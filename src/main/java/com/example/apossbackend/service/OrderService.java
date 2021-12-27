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

    OrderDTO findOrderByCustomerIdAndOrderId(long id, String accessToken);

    void cancelOrder(Long orderId, String cancelReason, String accessToken);

    void changeOrderStatus(long orderId, String accessToken, OrderStatus orderStatus);

    List<OrderDTO> findAllOrderByStatus(OrderStatus orderStatus, String accessToken);

    int countAllOnPlaceOrder(String accessToken);

    void cancelOrderSeller(long orderId, String cancelReason);
}
