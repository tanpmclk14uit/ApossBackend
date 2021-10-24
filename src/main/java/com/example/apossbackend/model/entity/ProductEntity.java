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
@Table(name= "product")
public class ProductEntity extends BaseEntity{

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kind_id",
            referencedColumnName = "id",
            nullable = false)
    private KindEntity kind;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String description;
}
