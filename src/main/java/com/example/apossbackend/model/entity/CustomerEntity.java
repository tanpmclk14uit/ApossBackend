package com.example.apossbackend.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="customer")
public class CustomerEntity extends BaseEntity{

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column()
    private String image;

    @Column()
    private Boolean gender;

    @Column(name = "birth_day")
    private Date birthDay;

    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderEntity> orders = new ArrayList<>();

    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DeliveryAddressEntity> deliveryAddresses = new ArrayList<>();

}
