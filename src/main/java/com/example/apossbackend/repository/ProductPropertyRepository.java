package com.example.apossbackend.repository;

import com.example.apossbackend.model.dto.ProductPropertyDTO;
import com.example.apossbackend.model.entity.ProductToClassifyProductValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductPropertyRepository extends JpaRepository<ProductToClassifyProductValueEntity, Long> {

    List<ProductToClassifyProductValueEntity> findProductToClassifyProductValueEntitiesByProductId(long id);

    @Query("SELECT DISTINCT new com.example.apossbackend.model.dto.ProductPropertyDTO(pp.id, pp.name,pp.propertyColor) \n" +
            "FROM ProductToClassifyProductValueEntity ppv \n" +
            "INNER JOIN ClassifyProductValueEntity pv on ppv.classifyProductValue.id = pv.id\n" +
            "INNER JOIN ClassifyProductEntity pp on pv.classifyProduct.id = pp.id\n" +
            "WHERE \n" +
            "ppv.product.id = :id \n" +
            "and pp.propertyColor = :isColor")
    List<ProductPropertyDTO> findProductPropertyIdByProductId(@Param("id") long id, @Param("isColor") boolean isColor);

}
