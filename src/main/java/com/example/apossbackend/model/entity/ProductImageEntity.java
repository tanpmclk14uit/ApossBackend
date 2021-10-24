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
@Table(name= "product_image")
public class ProductImageEntity extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",
            referencedColumnName = "id",
            nullable = false)
    private ProductEntity product;

    @Column(nullable = false)
    private int priority;

    @Column(nullable = false)
    private String imageUrl;
}
