package com.example.apossbackend.model.dto;

import lombok.Data;

@Data
public class SignInWithSocialDTO {
    private String email;
    private String name;
    private String imageURL;
    private String secretKey;
}
