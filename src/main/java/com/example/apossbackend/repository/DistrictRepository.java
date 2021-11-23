package com.example.apossbackend.repository;

import com.example.apossbackend.model.entity.DistrictEntity;
import com.example.apossbackend.model.entity.ProvinceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<DistrictEntity, Long> {
    public List<DistrictEntity> getDistrictEntityByProvince_Id(long province_id);
}
