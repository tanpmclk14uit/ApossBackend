package com.example.apossbackend.repository;

import com.example.apossbackend.model.entity.OrderEntity;
import com.example.apossbackend.utils.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
   List<OrderEntity> findOrderEntitiesByCustomerEmailAndStatus(String email, OrderStatus status);
   Optional<OrderEntity> getOrderEntityById(long id);

   Optional<OrderEntity> getOrderEntityByIdAndStatusAndCustomerEmail(long id, OrderStatus status, String customer_email);

}
