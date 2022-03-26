package com.example.apossbackend.repository;

import com.example.apossbackend.model.dto.ProductPropertyDTO;
import com.example.apossbackend.model.entity.SetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductPropertyRepository extends JpaRepository<SetEntity, Long> {

    List<SetEntity> findSetEntitiesByProductId (long id);
    Optional<SetEntity> findById(long id);

    @Query("SELECT DISTINCT new com.example.apossbackend.model.dto.ProductPropertyDTO(pp.id, pp.name,pp.propertyColor) \n" +
            "FROM SetEntity ppv \n" +
            "INNER JOIN SetValueEntity sv on ppv.id = sv.set.id\n" +
            "INNER JOIN ClassifyProductValueEntity pv on sv.classifyProductValue.id = pv.id\n" +
            "INNER JOIN ClassifyProductEntity pp on pv.classifyProduct.id = pp.id\n" +
            "WHERE \n" +
            "ppv.id = :id \n" +
            "and pp.propertyColor = :isColor")
    List<ProductPropertyDTO> findProductPropertyIdByProductId(@Param("id") long id, @Param("isColor") boolean isColor);



//    Boolean existsByProductIdAndClassifyProductValueId(long product_id, long classifyProductValue_id);

    SetEntity findSetEntityByProductIdAndSetValueEntityId(long product_id, long setValueEntity_id);
}
