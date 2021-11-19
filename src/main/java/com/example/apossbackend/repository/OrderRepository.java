package com.example.apossbackend.repository;

import com.example.apossbackend.model.entity.OrderEntity;
import com.example.apossbackend.utils.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
   List<OrderEntity> findOrderEntitiesByCustomerEmailAndStatus(String email, OrderStatus status);
}
