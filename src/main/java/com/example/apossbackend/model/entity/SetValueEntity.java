package com.example.apossbackend.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@ToString
@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "set_value")
public class SetValueEntity extends BaseEntity{
    @Id
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "set_id",
    referencedColumnName = "id",
    nullable = false)
    private SetEntity set;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "classify_product_value_id",
    referencedColumnName = "id",
    nullable = false)
    private ClassifyProductValueEntity classifyProductValue;
}
