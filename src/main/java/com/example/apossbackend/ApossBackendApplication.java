package com.example.apossbackend;

import com.example.apossbackend.model.entity.ProductEntity;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApossBackendApplication {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    public static void main(String[] args) {
        SpringApplication.run(ApossBackendApplication.class, args);
    }
}
