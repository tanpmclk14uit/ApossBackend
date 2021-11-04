package com.example.apossbackend.service;

import com.example.apossbackend.model.ProductResponse;
import com.example.apossbackend.model.dto.ProductDTO;
import org.springframework.data.domain.Page;


public interface ProductService {

    ProductResponse getAllProduct(int pageNumber, int pageSize);

}
