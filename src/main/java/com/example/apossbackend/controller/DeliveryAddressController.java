package com.example.apossbackend.controller;

import com.example.apossbackend.model.dto.DeliveryAddressDTO;
import com.example.apossbackend.security.JwtTokenProvider;
import com.example.apossbackend.service.DeliveryAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("api/v1/delivery_address")
public class DeliveryAddressController {

    private final DeliveryAddressService deliveryAddressService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public DeliveryAddressController(DeliveryAddressService deliveryAddressService, JwtTokenProvider jwtTokenProvider)
    {
        this.deliveryAddressService = deliveryAddressService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping
    public ResponseEntity<List<DeliveryAddressDTO>> getAllDeliveryAddressByCustomer(
            HttpServletRequest request
    )
    {
        String accessToken = jwtTokenProvider.getJWTFromRequest(request);
        return ResponseEntity.ok(deliveryAddressService.getAllDeliveryByCustomer(accessToken));
    }

    @GetMapping
    public ResponseEntity<DeliveryAddressDTO> getCurrentDefaultDeliveryAddressByCustomer(
            HttpServletRequest request
    )
    {
        String accessToken = jwtTokenProvider.getJWTFromRequest(request);
        return ResponseEntity.ok(deliveryAddressService.getCurrentDefaultDeliveryAddressByCustomer(accessToken));
    }

    @PostMapping
    public ResponseEntity<String> addNewDeliveryAddress(
            @RequestBody DeliveryAddressDTO deliveryAddressDTO,
            HttpServletRequest request
    )
    {
        String accessToken = jwtTokenProvider.getJWTFromRequest(request);
        deliveryAddressService.addNewDeliveryAddress(accessToken, deliveryAddressDTO);
        return ResponseEntity.ok("\"Add new delivery address success !\"");
    }

    @DeleteMapping("/{id}")
    public  ResponseEntity<String> deleteDeliveryAddress(
            @PathVariable(name = "id") Long deliveryAddressId,
            HttpServletRequest request
    ){
        String accessToken = jwtTokenProvider.getJWTFromRequest(request);
        deliveryAddressService.deleteDeliveryAddress(accessToken, deliveryAddressId);
        return ResponseEntity.ok("\"Delete delivery address success !\"");
    }

    @PutMapping()
    public ResponseEntity<String> updateDeliveryAddress(
            @RequestBody DeliveryAddressDTO deliveryAddressDTO,
            HttpServletRequest request
    ){
        String accessToken = jwtTokenProvider.getJWTFromRequest(request);
        deliveryAddressService.updateDeliveryAddress(accessToken, deliveryAddressDTO);
        return ResponseEntity.ok("\"Update delivery address success !\"");
    }
}
