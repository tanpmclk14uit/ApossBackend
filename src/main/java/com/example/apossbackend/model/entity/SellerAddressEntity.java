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
@Table(name="seller_address")
public class SellerAddressEntity extends BaseEntity {


    @Column(nullable = false)
    private long seller;

    @Column(nullable = false)
    private String address;

}
