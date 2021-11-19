package com.example.apossbackend.controller;

import com.example.apossbackend.model.dto.OrderDTO;
import com.example.apossbackend.security.JwtTokenProvider;
import com.example.apossbackend.service.OrderService;
import com.example.apossbackend.utils.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {

    private final OrderService orderService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public OrderController(OrderService orderService, JwtTokenProvider jwtTokenProvider) {
        this.orderService = orderService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping()
    public ResponseEntity<List<OrderDTO>> getAllOrderByCustomerIdAndStatus(
            HttpServletRequest request
    ){
        String accessToken = jwtTokenProvider.getJWTFromRequest(request);
        return ResponseEntity.ok(orderService.findAllOrderByCustomerIdAndStatus(OrderStatus.Pending, accessToken));
    }
}
