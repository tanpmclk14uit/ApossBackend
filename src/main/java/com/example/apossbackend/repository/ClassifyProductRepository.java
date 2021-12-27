package com.example.apossbackend.repository;

import com.example.apossbackend.model.entity.ClassifyProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassifyProductRepository extends JpaRepository<ClassifyProductEntity, Long> {
}
