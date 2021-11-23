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
@RequiredArgsConstructor
@ToString
@Entity
@Table(name = "rating")
public class RatingEntity extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id",
                referencedColumnName = "id",
                nullable = false)
    private CustomerEntity customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",
                referencedColumnName = "id",
                nullable = false)
    private ProductEntity product;

    @Column(nullable = false)
    private float rating;

    @Column(nullable = false)
    private String comment;

    @OneToMany(mappedBy = "rating", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RatingImageEntity> ratingImages = new ArrayList<>();
}
