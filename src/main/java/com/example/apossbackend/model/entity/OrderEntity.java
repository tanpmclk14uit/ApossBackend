package com.example.apossbackend.model.entity;

import com.example.apossbackend.utils.enums.OrderStatus;
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
@Table(name="orders")
public class OrderEntity extends BaseEntity {

    @Column(nullable = false)
    private long customer;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @Column(name = "received_date")
    private Date receivedDate;

    @Column(nullable = false, name = "total_price")
    private double totalPrice;

    @Column(nullable = false, name = "delivery_address")
    private String deliveryAddress;

    @Column(nullable = false)
    private long province;

    @Column(nullable = false)
    private  long district;

    @Column(nullable = false)
    private long ward;

    @Column(nullable = false)
    private OrderStatus status;

}
