package com.example.apossbackend.controller;
import com.example.apossbackend.exception.ApossBackendException;
import com.example.apossbackend.model.dto.JWTAuthResponse;
import com.example.apossbackend.model.dto.SignInDTO;
import com.example.apossbackend.model.dto.SignUpDTO;
import com.example.apossbackend.security.JwtTokenProvider;
import com.example.apossbackend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public AuthController(AuthService authService, JwtTokenProvider tokenProvider) {
        this.authService = authService;
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
    public ResponseEntity<String> registerCustomer(@RequestBody SignUpDTO signUpDTO) {
        if (authService.isEmailExist(signUpDTO.getEmail())) {
            return new ResponseEntity<>("Email is already taken!", HttpStatus.BAD_REQUEST);
        }
        authService.createCustomer(signUpDTO);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }

    @GetMapping("/access-token")
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
