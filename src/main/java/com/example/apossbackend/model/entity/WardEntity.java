package com.example.apossbackend.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="ward")
public class WardEntity{

    @Id
    private long id;

    @Column
    private String name;

    @ManyToOne
    @JoinColumn(nullable = false, name = "district")
    private DistrictEntity district;
}
