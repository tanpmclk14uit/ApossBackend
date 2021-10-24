package com.example.apossbackend.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="delivery_address")
public class DeliveryAddressEntity extends  BaseEntity{

    @ManyToOne
    @JoinColumn(nullable = false, name = "customer")
    private CustomerEntity customer;

    @Column(name = "phone", nullable = false)
    private String phoneNumber;

    @Column(nullable = false, name = "receiver_name")
    private String receiverName;

    @Column(nullable = false)
    private Boolean gender;

    @OneToOne
    @JoinColumn(nullable = false, name = "province")
    private ProvinceEntity province;

    @OneToOne
    @JoinColumn(nullable = false, name ="district")
    private DistrictEntity district;

    @OneToOne
    @JoinColumn(nullable = false, name = "ward")
    private WardEntity ward;

    @Column(nullable = false, name = "address_lane")
    private String addressLane;

    @Column(nullable = false, name="full_address")
    private String fullAddress;
}
