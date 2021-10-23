package com.example.apossbackend.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@ToString
@Getter
@Setter
@Entity
@RequiredArgsConstructor
@Table(name= "industry")
public class IndustryEntity extends BaseEntity{

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private float rating;

    @Column(nullable = false)
    private int purchased;

    @Column(nullable = false)
    private int totalProduct;
}
