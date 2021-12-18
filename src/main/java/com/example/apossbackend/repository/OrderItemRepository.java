package com.example.apossbackend.repository;

import com.example.apossbackend.model.entity.OrderEntity;
import com.example.apossbackend.model.entity.OrderItemEntity;
import com.example.apossbackend.utils.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
}
