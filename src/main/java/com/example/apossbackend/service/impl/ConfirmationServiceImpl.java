package com.example.apossbackend.service.impl;

import com.example.apossbackend.exception.ApossBackendException;
import com.example.apossbackend.exception.ResourceNotFoundException;
import com.example.apossbackend.model.entity.ConfirmationToken;
import com.example.apossbackend.model.entity.CustomerEntity;
import com.example.apossbackend.repository.ConfirmationRepository;
import com.example.apossbackend.repository.CustomerRepository;
import com.example.apossbackend.service.ConfirmationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ConfirmationServiceImpl implements ConfirmationService {

    private final ConfirmationRepository confirmationRepository;

    @Autowired
    public ConfirmationServiceImpl(ConfirmationRepository confirmationRepository) {
        this.confirmationRepository = confirmationRepository;
    }

    @Override
    public ConfirmationToken findByToken(String token) {
        return confirmationRepository.findByToken(token).orElseThrow(
                () -> new ResourceNotFoundException("Confirmation token", "token", token)
        );
    }

    @Override
    public String createNewToken(CustomerEntity customer) {
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                customer
        );
        confirmationRepository.save(confirmationToken);
        return confirmationToken.getToken();
    }

}
