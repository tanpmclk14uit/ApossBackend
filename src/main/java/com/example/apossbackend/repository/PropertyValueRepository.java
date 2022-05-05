package com.example.apossbackend.repository;

import com.example.apossbackend.model.entity.ClassifyProductValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyValueRepository extends JpaRepository<ClassifyProductValueEntity, Long> {
    List<ClassifyProductValueEntity> findClassifyProductValueEntitiesByClassifyProductId(Long classifyProductId);
    void deleteAllByClassifyProductId(Long id);
}
