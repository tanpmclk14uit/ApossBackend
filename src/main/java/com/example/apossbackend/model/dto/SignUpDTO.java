package com.example.apossbackend.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SignUpDTO {
    private Date birthDate;
    private String email;
    private String password;
    private Boolean gender;
    private String imageURL;
    private String name;
    private String phoneNumber;
}
