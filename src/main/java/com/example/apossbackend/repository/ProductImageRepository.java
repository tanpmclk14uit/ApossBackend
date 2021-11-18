package com.example.apossbackend.repository;

import com.example.apossbackend.model.entity.ProductEntity;
import com.example.apossbackend.model.entity.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long> {
    ProductImageEntity findProductImageEntityByProductAndPriority(ProductEntity productEntity, int priority);
}
