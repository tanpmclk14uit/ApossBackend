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
@Table(name= "product_to_classify_product_value")
public class ProductToClassifyProductValueEntity extends BaseEntity{

    @ManyToOne()
    @JoinColumn(name = "classify_product_value_id",
            referencedColumnName = "id",
            nullable = false)
    private ClassifyProductValueEntity classifyProductValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",
            referencedColumnName = "id",
            nullable = false)
    private ProductEntity product;

    @Column(nullable = false)
    private int additionalPrice;

    @Column(nullable = false)
    private int quantity;

}
