package com.example.apossbackend.repository;

import com.example.apossbackend.model.entity.ClassifyProductValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClassifyProductValueRepository extends JpaRepository<ClassifyProductValueEntity, Long> {

    List<ClassifyProductValueEntity> findClassifyProductValueEntitiesById(long id);

    @Override
    Optional<ClassifyProductValueEntity> findById(Long aLong);

}
