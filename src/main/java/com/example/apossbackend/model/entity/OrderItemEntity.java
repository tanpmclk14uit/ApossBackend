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
@Table(name="order_item")
public class OrderItemEntity extends BaseEntity {

    @Column(name = "order_id", nullable = false)
    private long order;

    @Column()
    private long product;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String property;

    @Column(nullable = false, name = "image")
    private String imageUrl;

    @Column(nullable = false)
    private double prince;

    @Column(nullable = false)
    private double quantity;
}
