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
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="province")
public class ProvinceEntity {

    @Id
    private long id;

    @Column
    private String name;

    @OneToMany(
            mappedBy = "province",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DistrictEntity> districts = new ArrayList<>();

    @Column
    private  String type;

    @Column
    private  String slug;
}
