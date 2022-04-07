package com.example.apossbackend.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@ToString
@Getter
@Setter
@Entity
@RequiredArgsConstructor
@Table(name= "set_of_product")
public class SetEntity extends BaseEntity{

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id",
            referencedColumnName = "id",
            nullable = false)
    private ProductEntity product;

    @Column(nullable = false)
    private int additionalPrice;

    @Column(nullable = false)
    private int quantity;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "set")
    private List<SetValueEntity> setValueEntity;

}
