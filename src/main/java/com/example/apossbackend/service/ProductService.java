package com.example.apossbackend.service;

import com.example.apossbackend.model.ProductsResponse;
import com.example.apossbackend.model.dto.ProductDetailDTO;
import com.example.apossbackend.model.dto.ProductImageDTO;

import java.util.List;


public interface ProductService {

    ProductsResponse getAllProduct(int pageNo, int pageSize, String sortBy, String sortDir);

    ProductsResponse getAllProductByKeyword(String keyword,int pageNo, int pageSize, String sortBy, String sortDir);

    ProductDetailDTO getProductDetail(long id);

    ProductsResponse getAllProductByKindId(long id, int pageNo, int pageSize);

    List<ProductImageDTO> getAllProductImageByProductId(long id);
}
