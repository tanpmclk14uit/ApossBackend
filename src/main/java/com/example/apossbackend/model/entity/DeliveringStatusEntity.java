package com.example.apossbackend.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="delivering_status")
public class DeliveringStatusEntity extends BaseEntity {

    @Column(nullable = false, name = "order_id")
    private long order;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Date time;

}
