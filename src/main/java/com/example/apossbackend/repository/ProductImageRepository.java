package com.example.apossbackend.repository;

import com.example.apossbackend.model.entity.ProductEntity;
import com.example.apossbackend.model.entity.ProductImageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImageEntity, Long> {
    ProductImageEntity findProductImageEntityByProductAndPriority(ProductEntity productEntity, int priority);

    Page<ProductImageEntity> findProductImageEntitiesByProductId(long id, Pageable pageable);

}
