package com.example.apossbackend.model.entity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ToString
@Getter
@Setter
@Entity
@RequiredArgsConstructor
@Table(name= "cart")
public class CartEntity extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",
            referencedColumnName = "id",
            nullable = false)
    private CustomerEntity customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "set_id",
            referencedColumnName = "id",
            nullable = false)
    private SetEntity set;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String property;

    @Column(nullable = false)
    private boolean isSelect;
}
