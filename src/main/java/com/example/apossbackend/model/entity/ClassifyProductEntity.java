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
@Table(name= "classify_product")
public class ClassifyProductEntity extends BaseEntity{

    @Column(nullable = false, name = "property_color")
    private boolean propertyColor;

    @Column(nullable = false)
    private String name;
}
