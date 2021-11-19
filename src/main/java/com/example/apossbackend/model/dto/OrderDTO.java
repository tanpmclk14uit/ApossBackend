package com.example.apossbackend.model.dto;

import com.example.apossbackend.utils.enums.OrderStatus;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderDTO {
    private long id;
    private Date orderTime;
    private OrderStatus orderStatus;
    private String address;
    private int totalPrice;
    private String cancelReason;
    private List<OrderItemDTO> orderItemDTOList;
}
