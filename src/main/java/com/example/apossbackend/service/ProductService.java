package com.example.apossbackend.service;

import com.example.apossbackend.model.ProductsResponse;
import com.example.apossbackend.model.dto.*;
import com.example.apossbackend.model.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;


public interface ProductService {

    ProductsResponse getAllProduct(int pageNo, int pageSize, String sortBy, String sortDir);

    ProductsResponse getAllProductByKeyword(String keyword,int pageNo, int pageSize, String sortBy, String sortDir);


    ProductDetailDTO getProductDetail(long id);

    ProductsResponse getAllProductByKindId(long id, int pageNo, int pageSize);

    List<ProductImageDTO> getAllProductImageByProductId(long id);

    List<ProductRatingDTO> getAllProductRatingOfProductId(long id);

    List<ProductPropertyDTO> getAllPropertyOfProductId(long id, boolean isColor);

}
