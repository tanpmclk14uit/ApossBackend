package com.example.apossbackend.repository;

import com.example.apossbackend.model.entity.CartEntity;
import com.example.apossbackend.model.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
    List<CartEntity> findCartEntitiesByCustomerEmail(String email);
    void deleteCartEntityById(long id);
    Optional<CartEntity>  findCartEntityById(long id);
}
