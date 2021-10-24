package com.example.apossbackend.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Setter
@ToString
@Getter
@RequiredArgsConstructor
@Table(name = "rating_image")
public class RatingImageEntity extends BaseEntity{

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rating_id",
                referencedColumnName = "id",
                nullable = false)
    private RatingEntity rating;
}
