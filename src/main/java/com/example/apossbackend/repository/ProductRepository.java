package com.example.apossbackend.repository;

import com.example.apossbackend.model.entity.ProductEntity;
import com.example.apossbackend.model.entity.ProductImageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    Page<ProductEntity> findAllByNameContains(String name, Pageable pageable);
    Page<ProductEntity> getProductEntitiesByNameContaining(String name, Pageable pageable);

    Page<ProductEntity> findProductEntityByKindId(long kindId, Pageable pageable);

    ProductEntity findProductEntityById(long id);

    @Transactional()
    @Modifying(clearAutomatically = true)
    @Query("update ProductEntity product set product.holdQuantity = :holdQuantity WHERE product.id = :productId")
    void setProductHoldQuantity(@Param("productId") Long id, @Param("holdQuantity") int holdQuantity);

    @Transactional()
    @Modifying( clearAutomatically = true)
    @Query("update ProductEntity product set product.quantity = :quantity WHERE product.id = :productId")
    void setProductQuantity(@Param("productId") Long id, @Param("quantity") int quantity);

}
