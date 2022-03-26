package com.example.apossbackend.repository;

import com.example.apossbackend.model.entity.SetValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SetValueRepository extends JpaRepository<SetValueEntity, Long> {
    List<SetValueEntity> findSetValueEntitiesBySetId(long set_id);
}
