package com.example.apossbackend.repository;

import com.example.apossbackend.model.entity.SetValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SetValueRepository extends JpaRepository<SetValueEntity, Long> {
    void deleteSetValueEntitiesBySetId(long id);
}
