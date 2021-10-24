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
@Table(name="seller_address")
public class SellerAddressEntity extends BaseEntity {


    @ManyToOne
    @JoinColumn(name = "seller")
    private SellerEntity seller;

    @Column(nullable = false)
    private String address;

}
