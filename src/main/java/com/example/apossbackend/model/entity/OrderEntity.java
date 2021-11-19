package com.example.apossbackend.model.entity;

import com.example.apossbackend.utils.enums.OrderStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name="orders")
public class OrderEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(nullable = false, name = "customer")
    private CustomerEntity customer;

    @Column(name = "cancel_reason")
    private String cancelReason;

    @Column(name = "received_date")
    private Date receivedDate;

    @Column(nullable = false, name = "total_price")
    private int totalPrice;

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

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItemEntity> items = new ArrayList<>();

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DeliveringStatusEntity> deliveryStatuses = new ArrayList<>();

}
