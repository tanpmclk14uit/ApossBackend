package com.example.apossbackend.model.entity;

import lombok.*;

import javax.persistence.*;

@ToString
@Getter
@Setter
@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name= "industry_image")
public class IndustryImageEntity extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "industry_id",
            referencedColumnName = "id",
            nullable = false)
    private IndustryEntity industry;

    @Column(nullable = false)
    private int priority;

    @Column(nullable = false)
    private String imageUrl;
}
