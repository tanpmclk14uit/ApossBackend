package com.example.apossbackend.repository;

import com.example.apossbackend.model.entity.KindEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KindRepository extends JpaRepository<KindEntity, Long> {
}
