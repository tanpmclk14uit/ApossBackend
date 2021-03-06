package com.example.apossbackend.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductRatingDTO {
    private long id;
    private String name;
    private float rating;
    private String time;
    private String content;
    private List<String> listImageURL;
    private String imageAvatarURl;
}
