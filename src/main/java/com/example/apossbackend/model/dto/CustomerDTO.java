package com.example.apossbackend.model.dto;

import lombok.Data;

import java.util.Date;

@Data
public class CustomerDTO {
    private Date birthDate;
    private String email;
    private Boolean gender;
    private String image;
    private String name;
    private String phoneNumber;
}
