package com.example.apossbackend.controller;

import com.example.apossbackend.model.dto.CartDTO;
import com.example.apossbackend.model.entity.CartEntity;
import com.example.apossbackend.security.JwtTokenProvider;
import com.example.apossbackend.service.CartService;
import org.apache.catalina.connector.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.callback.PrivateKeyCallback;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("api/v1/cart")
public class CartController {

    private final JwtTokenProvider jwtTokenProvider;
    private final CartService cartService;

    @Autowired
    public CartController(JwtTokenProvider jwtTokenProvider, CartService cartService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<String> createNewCart(
            HttpServletRequest request,
            @RequestBody CartDTO cartDTO) {
        String accessToken = jwtTokenProvider.getJWTFromRequest(request);
        cartService.createNewCart(cartDTO, accessToken);
        return ResponseEntity.ok("Create cart success");
    }
    @GetMapping
    public ResponseEntity<List<CartDTO>> getAllCart(HttpServletRequest request){
        String accessToken = jwtTokenProvider.getJWTFromRequest(request);
        return ResponseEntity.ok(cartService.getAllCartByCustomerAccessToken(accessToken));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCart(
            @PathVariable(value = "id") long id,
            HttpServletRequest request
    ){
        String accessToken = jwtTokenProvider.getJWTFromRequest(request);
        cartService.deleteCartEntityByIdAndToken(id, accessToken);
        return ResponseEntity.ok("Delete cart success");
    }

    @PutMapping()
    public ResponseEntity<String> updateCart(
            @RequestBody CartDTO cartDTO,
            HttpServletRequest request
    ){
        String accessToken = jwtTokenProvider.getJWTFromRequest(request);
        cartService.updateCartEntityByIdAndToken(cartDTO, accessToken);
        return ResponseEntity.ok("Update cart success");
    }

}
