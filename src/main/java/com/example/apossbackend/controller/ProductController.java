package com.example.apossbackend.controller;

import com.example.apossbackend.exception.ResourceNotFoundException;
import com.example.apossbackend.model.ProductResponse;
import com.example.apossbackend.model.entity.ProductEntity;
import com.example.apossbackend.service.ProductService;
import com.example.apossbackend.utils.AppConstants;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        super();
        this.productService = productService;
    }

    @GetMapping
    public ProductResponse getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize
    ){
        return productService.getAllProduct(pageNo, pageSize);
    }

}
