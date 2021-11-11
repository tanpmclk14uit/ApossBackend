package com.example.apossbackend.service;

import com.example.apossbackend.model.ProductsResponse;



public interface ProductService {

    ProductsResponse getAllProduct(int pageNo, int pageSize, String sortBy, String sortDir);

    ProductsResponse getAllProductByKeyword(String keyword,int pageNo, int pageSize, String sortBy, String sortDir);
}
