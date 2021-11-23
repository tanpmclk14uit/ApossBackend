package com.example.apossbackend.repository;

import com.example.apossbackend.model.entity.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RatingRepository extends JpaRepository<RatingEntity, Long> {
    int countAllByProductId(long productId);
}
