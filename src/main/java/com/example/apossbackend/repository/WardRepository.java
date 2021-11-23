package com.example.apossbackend.repository;

import com.example.apossbackend.model.entity.WardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WardRepository extends JpaRepository<WardEntity, Long> {
    public List<WardEntity> getAllByDistrict_Id(long district_id);
}
