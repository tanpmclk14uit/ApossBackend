package com.example.apossbackend.repository;

import com.example.apossbackend.model.entity.ProductEntity;
import com.example.apossbackend.model.entity.ProductImageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Page<ProductEntity> findAllByNameContains(String name, Pageable pageable);

    Page<ProductEntity> findProductEntityByKindId(long kindId, Pageable pageable);
}
