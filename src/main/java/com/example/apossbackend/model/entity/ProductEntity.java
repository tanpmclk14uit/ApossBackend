package com.example.apossbackend.model.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "product")
public class ProductEntity extends BaseEntity{

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int quantity;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kind_id",
            referencedColumnName = "id",
            nullable = false
    )
    private KindEntity kind;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String description;

}
