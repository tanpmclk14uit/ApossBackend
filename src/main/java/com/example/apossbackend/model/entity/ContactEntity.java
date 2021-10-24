package com.example.apossbackend.model.entity;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="contact")
public class ContactEntity extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String value;

    @Column(nullable = false)
    private long seller;
}
