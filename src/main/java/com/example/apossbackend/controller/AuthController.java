package com.example.apossbackend.controller;

import com.example.apossbackend.model.dto.SignInDTO;
import com.example.apossbackend.model.dto.SignUpDTO;
import com.example.apossbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signIn")
    public ResponseEntity<String> authenticateUser(@RequestBody SignInDTO signInDTO){
        SecurityContextHolder.getContext().setAuthentication(authService.signIn(signInDTO.getEmail(), signInDTO.getPassword()));
        return new ResponseEntity<>("User signed-in successfully!", HttpStatus.OK);
    }
    @PostMapping("/signUp")
    public ResponseEntity<String> registerCustomer(@RequestBody SignUpDTO signUpDTO){
        if(authService.isEmailExist(signUpDTO.getEmail())){
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }
        authService.createCustomer(signUpDTO);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }

}
