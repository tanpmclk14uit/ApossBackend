package com.example.apossbackend.repository;

import com.example.apossbackend.model.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    Optional<CustomerEntity> findByEmail(String email);
    Boolean existsByEmail(String email);
}
