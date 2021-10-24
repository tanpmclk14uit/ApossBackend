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
@Table(name= "classify_product_value")
public class ClassifyProductValueEntity extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "classify_product_id",
            referencedColumnName = "id",
            nullable = false)
    private ClassifyProductEntity classifyProduct;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String value;
}
