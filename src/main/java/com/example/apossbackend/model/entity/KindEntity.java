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
@Table(name= "kind")
public class KindEntity extends BaseEntity{

    @ManyToOne()
    @JoinColumn(name = "industry_id",
            referencedColumnName = "id",
            nullable = false)
    private IndustryEntity industry;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private float rating;

    @Column(nullable = false)
    private int purchased;

    @Column(nullable = false)
    private int totalProduct;
}
