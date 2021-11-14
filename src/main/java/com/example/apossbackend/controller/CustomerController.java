package com.example.apossbackend.controller;

import com.example.apossbackend.model.dto.CustomerDTO;
import com.example.apossbackend.security.JwtTokenProvider;
import com.example.apossbackend.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }


   @GetMapping("/{email}")
    public ResponseEntity<CustomerDTO> getCustomerByEmail(
            @RequestBody String accessToken,
            @PathVariable(name ="email") String email
   ){
        return ResponseEntity.ok(customerService.findUserInformationByEmail(email, accessToken));
   }
}
