package com.example.apossbackend.controller;

import com.example.apossbackend.model.dto.CustomerDTO;
import com.example.apossbackend.security.JwtTokenProvider;
import com.example.apossbackend.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;


    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

   @GetMapping("/{token}")
   public ResponseEntity<CustomerDTO> getCustomer(@PathVariable(name = "token") String token){
       return ResponseEntity.ok(customerService.findUserInformationByToken(token));
   }


}
