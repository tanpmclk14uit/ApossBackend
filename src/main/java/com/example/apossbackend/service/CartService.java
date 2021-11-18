package com.example.apossbackend.service;

import com.example.apossbackend.model.dto.CartDTO;
import com.example.apossbackend.model.entity.CartEntity;

import java.util.List;

public interface CartService {
    CartEntity createNewCart(CartDTO cartDTO, String accessToken);
    List<CartDTO> getAllCartByCustomerAccessToken(String accessToken);
    void deleteCartEntityByIdAndToken(long  id, String accessToken);
    void updateCartEntityByIdAndToken(CartDTO cartDTO, String accessToken);

}
