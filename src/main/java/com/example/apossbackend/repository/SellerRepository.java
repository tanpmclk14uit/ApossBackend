package com.example.apossbackend.repository;

import com.example.apossbackend.model.entity.SellerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<SellerEntity, Long> {
    boolean existsByEmail(String email);

    Optional<SellerEntity> findByEmail(String email);
}
