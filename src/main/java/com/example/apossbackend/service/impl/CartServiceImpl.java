package com.example.apossbackend.service.impl;

import com.example.apossbackend.exception.ApossBackendException;
import com.example.apossbackend.exception.ResourceNotFoundException;
import com.example.apossbackend.model.dto.CartDTO;
import com.example.apossbackend.model.entity.CartEntity;
import com.example.apossbackend.model.entity.CustomerEntity;
import com.example.apossbackend.model.entity.ProductEntity;
import com.example.apossbackend.repository.CartRepository;
import com.example.apossbackend.repository.CustomerRepository;
import com.example.apossbackend.repository.ProductRepository;
import com.example.apossbackend.security.JwtTokenProvider;
import com.example.apossbackend.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {


    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public CartServiceImpl(CustomerRepository customerRepository, ProductRepository productRepository, CartRepository cartRepository, JwtTokenProvider tokenProvider) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public CartEntity createNewCart(CartDTO cartDTO, String accessToken) {
        String userName = tokenProvider.getUsernameFromJWT(accessToken);
        CartEntity cartEntity = mapToCartEntity(cartDTO, userName);
        cartRepository.save(cartEntity);
        return cartEntity;
    }

    @Override
    public List<CartDTO> getAllCartByCustomerAccessToken(String accessToken) {
        String userName = tokenProvider.getUsernameFromJWT(accessToken);
        List<CartEntity> cartEntities = cartRepository.findCartEntitiesByCustomerEmail(userName);
        return cartEntities.stream().map(this::mapToCartDTO).collect(Collectors.toList());
    }

    @Override
    public void updateCartEntityByIdAndToken(CartDTO cartDTO, String accessToken) {
        CartEntity cartEntity = cartRepository.findCartEntityById(cartDTO.getId()).orElseThrow(
                () -> new ResourceNotFoundException("Cart", "id", cartDTO.getId())
        );
        String userName = tokenProvider.getUsernameFromJWT(accessToken);
        if(cartEntity.getCustomer().getEmail().equals(userName)){
            cartEntity.setProperty(cartDTO.getProperty());
            cartEntity.setUpdateTime(new Timestamp(new Date().getTime()));
            cartEntity.setQuantity(cartDTO.getQuantity());
            cartRepository.save(cartEntity);
        }else {
            throw  new ApossBackendException(HttpStatus.BAD_REQUEST, "You don't have permission to do this action!");
        }
    }

    @Override
    public void deleteCartEntityByIdAndToken(long id, String accessToken) {
        CartEntity cartEntity = cartRepository.findCartEntityById(id).orElseThrow(
                () -> new ResourceNotFoundException("Cart", "id", id)
        );
        String userName = tokenProvider.getUsernameFromJWT(accessToken);
        if(cartEntity.getCustomer().getEmail().equals(userName)){
            cartRepository.delete(cartEntity);
        }else {
            throw  new ApossBackendException(HttpStatus.BAD_REQUEST, "You don't have permission to do this action!");
        }
    }



    private CartEntity mapToCartEntity(CartDTO cartDTO, String userName) {
        CartEntity cartEntity = new CartEntity();
        cartEntity.setCreateTime(new Timestamp(new Date().getTime()));
        cartEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        CustomerEntity customer = customerRepository.findByEmail(userName).orElseThrow(
                () -> new ApossBackendException("Error", HttpStatus.BAD_REQUEST, "Not found customer")
        );
        cartEntity.setCustomer(customer);
        ProductEntity productEntity = productRepository.findById(cartDTO.getProductId()).orElseThrow(
                () -> new ApossBackendException("Error", HttpStatus.BAD_REQUEST, "Not found product")
        );
        cartEntity.setProduct(productEntity);
        cartEntity.setQuantity(cartDTO.getQuantity());
        cartEntity.setProperty(cartDTO.getProperty());
        return cartEntity;
    }

    private CartDTO mapToCartDTO(CartEntity cartEntity) {
        CartDTO cartDTO = new CartDTO();
        long productId = cartEntity.getProduct().getId();
        cartDTO.setId(cartEntity.getId());
        cartDTO.setProductId(productId);
        cartDTO.setProperty(cartEntity.getProperty());
        cartDTO.setQuantity(cartEntity.getQuantity());
        ProductEntity productEntity = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Cart", "product id", productId )
        );
        cartDTO.setName(productEntity.getName());
        cartDTO.setImageUrl(productEntity.getProductImages().get(0).getImageUrl());
        cartDTO.setPrice(productEntity.getPrice());
        return cartDTO;
    }

}