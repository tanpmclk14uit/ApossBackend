package com.example.apossbackend.repository;

import com.example.apossbackend.model.entity.IndustryImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndustryImageRepository extends JpaRepository<IndustryImageEntity, Long> {
}
