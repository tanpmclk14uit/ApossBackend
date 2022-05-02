package com.example.apossbackend.repository;

import com.example.apossbackend.model.dto.ProductPropertyDTO;
import com.example.apossbackend.model.dto.ProductPropertyValueDTO;
import com.example.apossbackend.model.entity.SetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductPropertyRepository extends JpaRepository<SetEntity, Long> {


    @Query("SELECT DISTINCT new com.example.apossbackend.model.dto.ProductPropertyDTO(pp.id, pp.name,pp.propertyColor) \n" +
            "FROM SetValueEntity sv \n" +
            "INNER JOIN SetEntity sop on sop.id = sv.set.id\n" +
            "INNER JOIN ClassifyProductValueEntity pv on sv.classifyProductValue.id = pv.id\n" +
            "INNER JOIN ClassifyProductEntity pp on pv.classifyProduct.id = pp.id\n" +
            "WHERE \n" +
            "sop.product.id  = :id \n" +
            "and pp.propertyColor = :isColor")
    List<ProductPropertyDTO> findProductPropertyIdByProductId(@Param("id") long id, @Param("isColor") boolean isColor);

    @Query("select distinct new com.example.apossbackend.model.dto.ProductPropertyValueDTO(pv.id, pv.name, pv.value) \n" +
            "FROM SetValueEntity sv \n" +
            "INNER JOIN SetEntity sop on sop.id = sv.set.id\n" +
            "INNER JOIN ClassifyProductValueEntity pv on sv.classifyProductValue.id = pv.id\n" +
            "INNER JOIN ClassifyProductEntity pp on pv.classifyProduct.id = pp.id\n" +
            "WHERE \n" +
            "sop.product.id  = :product_id \n" +
            "and pp.id = :property_id"
    )
    List<ProductPropertyValueDTO> findProductPropertyValueByProductIdAndPropertyId(@Param("product_id") long productId, @Param("property_id") long propertyId);

    @Query("select sop.quantity FROM SetValueEntity sv " +
            "left join SetEntity sop on sop.id = sv.set.id " +
            "WHERE sv.classifyProductValue.id in :propertyValues " +
            "and sop.product.id = :productId " +
            "group by sop.id " +
            "having count(distinct sv.classifyProductValue.id) = :propertyValuesCount")
    List<Integer> getTotalQuantityOfProductBySpecifyPropertyValuesIdAndProductId(@Param("productId") long productId, @Param("propertyValues") List<Long> propertyValuesId, @Param("propertyValuesCount") long propertyValuesCount);

    @Query("select sop.additionalPrice FROM SetValueEntity sv " +
            "left join SetEntity sop on sop.id = sv.set.id " +
            "WHERE sv.classifyProductValue.id in :propertyValues " +
            "and sop.product.id = :productId " +
            "group by sop.id " +
            "having count(distinct sv.classifyProductValue.id) = :propertyValuesCount")
    List<Integer> getAdditionalPriceOfProductBySpecifyPropertyValuesIdAndProductId(@Param("productId") long productId, @Param("propertyValues") List<Long> propertyValuesId, @Param("propertyValuesCount") long propertyValuesCount);

    @Query("select sop.id  FROM SetValueEntity sv " +
            "left join SetEntity sop on sop.id = sv.set.id " +
            "WHERE sv.classifyProductValue.id in :propertyValues " +
            "and sop.product.id = :productId " +
            "group by sop.id " +
            "having count(distinct sv.classifyProductValue.id) = :propertyValuesCount")
    List<Long> getSetOfProductBySpecifyPropertyValuesIdAndProductId(@Param("productId") long productId, @Param("propertyValues") List<Long> propertyValuesId, @Param("propertyValuesCount") long propertyValuesCount);

    void deleteSetEntitiesByProductId(long id);

    List<SetEntity> getSetEntitiesByProductId(long id);

    boolean existsSetEntityByProductId(long id);

    @Transactional()
    @Modifying( clearAutomatically = true)
    @Query("update SetEntity setEntity set setEntity.quantity = :quantity WHERE setEntity.id = :setId")
    void updateSetQuantity(@Param("setId") Long id, @Param("quantity") int quantity);
}
