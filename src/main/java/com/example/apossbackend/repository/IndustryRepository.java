package com.example.apossbackend.repository;

import com.example.apossbackend.model.entity.IndustryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndustryRepository extends JpaRepository<IndustryEntity, Long> {
    boolean existsIndustryEntityById(long id);
}
