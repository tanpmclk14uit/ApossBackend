package com.example.apossbackend.controller;

import com.example.apossbackend.model.dto.CustomerDTO;
import com.example.apossbackend.security.JwtTokenProvider;
import com.example.apossbackend.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final JwtTokenProvider jwtTokenProvider;

    public CustomerController(CustomerService customerService, JwtTokenProvider jwtTokenProvider) {
        this.customerService = customerService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/{email}")
    public ResponseEntity<CustomerDTO> getCustomerByEmail(
            @PathVariable(name ="email") String email,
            HttpServletRequest request
   ){
        String accessToken = jwtTokenProvider.getJWTFromRequest(request);
        return ResponseEntity.ok(customerService.findUserInformationByEmail(email, accessToken));
   }
}
