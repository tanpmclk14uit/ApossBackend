package com.example.apossbackend.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
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
    private int totalProduct;

    @Column(nullable = false)
    private String image;

    @OneToMany(mappedBy = "kind", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> products= new ArrayList<>();

}
