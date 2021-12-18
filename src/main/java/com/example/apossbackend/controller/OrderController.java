package com.example.apossbackend.controller;

import com.example.apossbackend.model.dto.OrderDTO;
import com.example.apossbackend.model.dto.OrderItemDTO;
import com.example.apossbackend.security.JwtTokenProvider;
import com.example.apossbackend.service.OrderService;
import com.example.apossbackend.utils.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        System.out.println("in it ok");
        String accessToken = jwtTokenProvider.getJWTFromRequest(request);
        return ResponseEntity.ok(orderService.findAllOrderByCustomerIdAndStatus(OrderStatus.Pending, accessToken));
    }

    @PostMapping
    public ResponseEntity<String> addNewOrder(
            @RequestBody OrderDTO orderDTO,
            HttpServletRequest request
    )
    {
        String accessToken = jwtTokenProvider.getJWTFromRequest(request);
        orderService.addNewOrder(accessToken, orderDTO);
        return ResponseEntity.ok("\"Add new order success !\"");
    }

    @PutMapping("/hold")
    public ResponseEntity<String> holdProduct(
            @RequestBody List<OrderItemDTO> listOrderItemDTO,
            HttpServletRequest request
    ){
        System.out.println("hold service");
        String accessToken = jwtTokenProvider.getJWTFromRequest(request);
        orderService.holdOrder(accessToken, listOrderItemDTO);
        return ResponseEntity.ok("\"Hold success !\"");
    }

    @PutMapping("/reduceHold")
    public ResponseEntity<String> reduceHold(
            @RequestBody List<OrderItemDTO> listOrderItemDTO,
            HttpServletRequest request
    ){
        System.out.println("hold service");
        String accessToken = jwtTokenProvider.getJWTFromRequest(request);
        orderService.reduceHold(accessToken, listOrderItemDTO);
        return ResponseEntity.ok("\"Hold success !\"");
    }
}
