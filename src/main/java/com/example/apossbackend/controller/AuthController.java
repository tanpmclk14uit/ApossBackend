package com.example.apossbackend.controller;
import com.example.apossbackend.exception.ApossBackendException;
import com.example.apossbackend.model.dto.JWTAuthResponse;
import com.example.apossbackend.model.dto.SignInDTO;
import com.example.apossbackend.model.dto.SignUpDTO;
import com.example.apossbackend.model.entity.ConfirmationToken;
import com.example.apossbackend.model.entity.CustomerEntity;
import com.example.apossbackend.security.JwtTokenProvider;
import com.example.apossbackend.service.AuthService;
import com.example.apossbackend.service.ConfirmationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final ConfirmationService confirmationService;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public AuthController(AuthService authService, ConfirmationService confirmationService, JwtTokenProvider tokenProvider) {
        this.authService = authService;
        this.confirmationService = confirmationService;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody SignInDTO signInDTO) {
        Authentication authentication = authService.signIn(signInDTO.getEmail(), signInDTO.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(signInDTO.getEmail(), signInDTO.getPassword());
        return ResponseEntity.ok(new JWTAuthResponse(token, refreshToken));
    }

    @PostMapping("/sign-up")
    @Transactional
    public ResponseEntity<String> registerCustomer(@RequestBody SignUpDTO signUpDTO) {
        if (authService.isEmailExist(signUpDTO.getEmail())) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }
        CustomerEntity customer = authService.createCustomer(signUpDTO);
        confirmationService.createNewToken(customer);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }
    @PostMapping("/confirm")
    public ResponseEntity<String> validateCustomer(@RequestParam("token") String token){
        ConfirmationToken confirmationToken = confirmationService.findByToken(token);
        if(authService.updateActivatedByToken(confirmationToken)){
            return new ResponseEntity<>("Confirm account success", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Confirm account failure", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/access-token")
    public ResponseEntity<String> getNewAccessToken(@RequestBody String refreshToken) {
        if(refreshToken.startsWith("\"") && refreshToken.endsWith("\"")){
            refreshToken = refreshToken.substring(1, refreshToken.length()-1);
        }
        if(!tokenProvider.validateToken(refreshToken)){
            throw new ApossBackendException(HttpStatus.BAD_REQUEST,"Refresh token error");
        }
        SignInDTO signInDTO = tokenProvider.getUserNamePasswordFromRefreshToken(refreshToken);
        Authentication authentication = authService.signIn(signInDTO.getEmail(), signInDTO.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(token);
    }
}
