package com.example.apossbackend.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="seller")
public class SellerEntity extends BaseEntity {

    @Column(nullable = false)
    private String account;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(name = "phone", nullable = false)
    private String phoneNumber;

    @OneToMany(
            mappedBy = "seller",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SellerImageEntity> images = new ArrayList<>();

    @OneToMany(
            mappedBy = "seller",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<SellerAddressEntity> addresses = new ArrayList<>();

    @OneToMany(
            mappedBy = "seller",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ContactEntity> contacts = new ArrayList<>();
}
