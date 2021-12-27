package com.example.apossbackend.service.impl;

import com.example.apossbackend.exception.ApossBackendException;
import com.example.apossbackend.exception.ResourceNotFoundException;
import com.example.apossbackend.model.dto.SignInWithSocialDTO;
import com.example.apossbackend.model.dto.SignUpDTO;
import com.example.apossbackend.model.entity.ConfirmationToken;
import com.example.apossbackend.model.entity.CustomerEntity;
import com.example.apossbackend.repository.ConfirmationRepository;
import com.example.apossbackend.repository.CustomerRepository;
import com.example.apossbackend.repository.SellerRepository;
import com.example.apossbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;
    private final ConfirmationRepository confirmationRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SellerRepository sellerRepository;

    @Autowired
    public AuthServiceImpl(CustomerRepository customerRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                           SellerRepository sellerRepository, ConfirmationRepository confirmationRepository) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.sellerRepository = sellerRepository;
        this.confirmationRepository = confirmationRepository;
    }

    @Override
    public boolean isEmailExist(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public CustomerEntity createCustomer(SignUpDTO signUpDTO) {
        CustomerEntity customer = new CustomerEntity();
        customer.setEmail(signUpDTO.getEmail());
        customer.setPassword(passwordEncoder.encode(signUpDTO.getPassword()));
        customer.setImage(signUpDTO.getImageURL());
        customer.setBirthDay(signUpDTO.getBirthDate());
        customer.setName(signUpDTO.getName());
        customer.setPhoneNumber(signUpDTO.getPhoneNumber());
        customer.setGender(signUpDTO.getGender());
        customer.setCreateTime(new Timestamp(new Date().getTime()));
        customer.setUpdateTime(new Timestamp(new Date().getTime()));
        customerRepository.save(customer);
        return customer;
    }

    @Override
    public Authentication signIn(String email, String password) {
        CustomerEntity customer = customerRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("Account", "Email", email));
        if (!customer.isActive()) {
            throw new ApossBackendException(HttpStatus.BAD_REQUEST, "Account was not activated!");
        }
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                email, password));
    }

    @Override
    @Transactional
    public Boolean updateActivatedByToken(ConfirmationToken token) {
        CustomerEntity customer = token.getCustomer();
        if(token.getConfirmedAt() != null){
            throw new ApossBackendException(HttpStatus.BAD_REQUEST,"Account was already confirmed");
        }
        if(token.getExpiredAt().isBefore(LocalDateTime.now())){
            throw new ApossBackendException(HttpStatus.BAD_REQUEST, "Token expired");
        }
        token.setConfirmedAt(LocalDateTime.now());
        confirmationRepository.save(token);
        customer.setActive(true);
        customerRepository.save(customer);
        return true;
    }

    @Override
    @Transactional
    public void signInWithGoogle(SignInWithSocialDTO signInWithSocialDTO, String password) {
        if(!customerRepository.existsByEmail(signInWithSocialDTO.getEmail())){
            CustomerEntity customer = new CustomerEntity();
            customer.setEmail(signInWithSocialDTO.getEmail());
            customer.setPassword(passwordEncoder.encode(password));
            customer.setName(signInWithSocialDTO.getName());
            customer.setActive(true);
            customer.setCreateTime(new Timestamp(new Date().getTime()));
            customer.setUpdateTime(new Timestamp(new Date().getTime()));
            if(!signInWithSocialDTO.getImageURL().equalsIgnoreCase("null")){
                customer.setImage(signInWithSocialDTO.getImageURL());
            }
            customerRepository.save(customer);
        }
    }

    @Override
    public Authentication signInSeller(String email, String password) {
        if(!sellerRepository.existsByEmail(email)){
            throw new ResourceNotFoundException("Account","Email",email);
        }
        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                email, password));
    }
}
