package com.example.apossbackend.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="delivery_address")
public class DeliveryAddressEntity extends  BaseEntity{

    @Column(nullable = false)
    private long customer;

    @Column(name = "phone", nullable = false)
    private String phoneNumber;

    @Column(nullable = false, name = "receiver_name")
    private String receiverName;

    @Column(nullable = false)
    private Boolean gender;

    @Column(nullable = false)
    private long province;

    @Column(nullable = false)
    private long district;

    @Column(nullable = false)
    private long ward;

    @Column(nullable = false, name = "address_lane")
    private String addressLane;

    @Column(nullable = false, name="full_address")
    private String fullAddress;
}
