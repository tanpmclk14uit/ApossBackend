package com.example.apossbackend.repository;

import com.example.apossbackend.model.entity.DeliveryAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddressEntity, Long> {
    List<DeliveryAddressEntity> findAllByCustomer_Email(String customer_email);
    Optional<DeliveryAddressEntity> findDeliveryAddressEntityById(long id);
    DeliveryAddressEntity findDeliveryAddressEntitiesByIsDefaultIsTrueAndCustomer_Email(String customer_email);
}
