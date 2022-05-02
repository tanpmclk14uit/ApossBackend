package com.example.apossbackend.repository;

import com.example.apossbackend.model.entity.ClassifyProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<ClassifyProductEntity, Long> {
}
