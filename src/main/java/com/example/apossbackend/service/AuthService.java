package com.example.apossbackend.service;

import com.example.apossbackend.model.dto.SignUpDTO;
import org.springframework.security.core.Authentication;

public interface AuthService {

    boolean isEmailExist(String email);
    CustomerEntity createCustomer(SignUpDTO signUpDTO);
    Authentication signIn(String email, String password);
    Authentication signInSeller(String email, String password);
    void signInWithGoogle(SignInWithSocialDTO signInWithSocialDTO, String password);
    Boolean updateActivatedByToken(ConfirmationToken token);
}
