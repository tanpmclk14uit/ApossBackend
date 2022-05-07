package com.example.apossbackend.model.dto;

import com.example.apossbackend.utils.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class OrderDTO {
    private long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderTime;
    private OrderStatus orderStatus;
    private String address;
    private int totalPrice;
    private String cancelReason;
    private String customerEmail;
    private List<OrderItemDTO> orderItemDTOList;
}
