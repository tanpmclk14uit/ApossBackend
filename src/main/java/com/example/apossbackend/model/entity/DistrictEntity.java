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
@Table(name="district")
public class DistrictEntity {

    @Id
    private long id;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false, name = "province")
    private ProvinceEntity province;

    @OneToMany(
            mappedBy = "district",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<WardEntity> wards = new ArrayList<>();
}
