package com.example.apossbackend.repository;

import com.example.apossbackend.model.entity.SetValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SetValueRepository extends JpaRepository<SetValueEntity, Long> {
    void deleteSetValueEntitiesBySetId(long id);

    boolean existsSetValueEntityByClassifyProductValueId(long id);

    @Query("select count(sv.id) from " +
            "SetValueEntity sv inner join ClassifyProductValueEntity cpv on sv.classifyProductValue.id = cpv.id " +
            "where cpv.classifyProduct.id = :propertyId")
    int countSetValueEntitiesByClassifyProductId(@Param("propertyId") long propertyId);


}
