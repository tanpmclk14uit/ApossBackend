package com.example.apossbackend.controller;

import com.example.apossbackend.model.dto.OrderDTO;
import com.example.apossbackend.security.JwtTokenProvider;
import com.example.apossbackend.service.OrderService;
import com.example.apossbackend.utils.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    ) {
        String accessToken = jwtTokenProvider.getJWTFromRequest(request);
        return ResponseEntity.ok(orderService.findAllOrderByCustomerIdAndStatus(OrderStatus.Pending, accessToken));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDTO>> getAllOrder() {
        return ResponseEntity.ok(orderService.getAllOrder());
    }

    @PostMapping
    public ResponseEntity<String> addNewOrder(
            @RequestBody OrderDTO orderDTO,
            HttpServletRequest request
    ) {
        String accessToken = jwtTokenProvider.getJWTFromRequest(request);
        orderService.addNewOrder(accessToken, orderDTO);
        return ResponseEntity.ok("\"Add new order success !\"");
    }


    @GetMapping("/all-order-by")
    public ResponseEntity<List<OrderDTO>> getAllOrderByCustomerAndStatus(
            @RequestParam("status") OrderStatus orderStatus,
            HttpServletRequest request
    ) {
        String accessToken = jwtTokenProvider.getJWTFromRequest(request);
        return ResponseEntity.ok(orderService.findAllOrderByCustomerIdAndStatus(orderStatus, accessToken));
    }

    @GetMapping("/order-by-id/{id}")
    public ResponseEntity<OrderDTO> getAllOrderByCustomerAndStatus(
            @PathVariable(value = "id") long id,
            HttpServletRequest request
    ) {
        String accessToken = jwtTokenProvider.getJWTFromRequest(request);
        return ResponseEntity.ok(orderService.findOrderByCustomerIdAndOrderId(id, accessToken));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<String> cancelOrder(
            @PathVariable(value = "id") long orderId,
            @RequestBody String cancelReason,
            HttpServletRequest request
    ) {
        String accessToken = jwtTokenProvider.getJWTFromRequest(request);
        orderService.cancelOrder(orderId, cancelReason, accessToken);
        return ResponseEntity.ok("\"Cancel order success\"");
    }

    @PutMapping("{id}/address")
    public ResponseEntity<String> changeOrderAddress(
            @PathVariable(value = "id") long orderId,
            @RequestBody String newAddress,
            HttpServletRequest request
    ) {
        String accessToken = jwtTokenProvider.getJWTFromRequest(request);
        orderService.changeOrderAddress(orderId, newAddress, accessToken);
        return ResponseEntity.ok("\"Change address success\"");
    }


    @PutMapping("/success-order-customer/{id}")
    public ResponseEntity<String> makeSuccessOrder(
            @PathVariable(value = "id") long orderId,
            HttpServletRequest request
    ) {
        String accessToken = jwtTokenProvider.getJWTFromRequest(request);
        orderService.makeSuccessOrder(orderId, accessToken);
        return ResponseEntity.ok("\"Update order success\"");
    }

    @PutMapping("/change-order-status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> changeOrderStatus(
            @PathVariable(value = "id") long orderId,
            @RequestParam("status") OrderStatus orderStatus
    ) {
        orderService.changeOrderStatus(orderId, orderStatus);
        return ResponseEntity.ok("\"Update order success\"");
    }

    @GetMapping("/order-by-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderDTO>> getAllOrderByStatus(
            @RequestParam("status") OrderStatus orderStatus
    ) {
        List<OrderDTO> listOrder = orderService.findAllOrderByStatus(orderStatus);
        System.out.println(listOrder.size());
        return ResponseEntity.ok(listOrder);
    }

    @GetMapping("/on-place-order")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Integer> countAllOnPlaceOrder() {
        return ResponseEntity.ok(orderService.countAllOnPlaceOrder());
    }

    @PutMapping("/cancel-order-seller/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> cancelOrderSeller(
            @PathVariable(value = "id") long orderId,
            @RequestBody String cancelReason
    ) {
        orderService.cancelOrderSeller(orderId, cancelReason);
        return ResponseEntity.ok("\"Update order success\"");
    }


}

