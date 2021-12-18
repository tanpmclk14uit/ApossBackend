package com.example.apossbackend.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="order_item")
public class OrderItemEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @Column()
    private long product;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String property;

    @Column(nullable = false, name = "image")
    private String imageUrl;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int quantity;
}
