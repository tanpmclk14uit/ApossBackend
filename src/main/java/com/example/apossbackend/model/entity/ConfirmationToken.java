package com.example.apossbackend.model.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "confirmation_token")
public class ConfirmationToken{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    @Column
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "customer"
    )
    private CustomerEntity customer;

    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiredAt, CustomerEntity customer) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.customer = customer;
    }
}
