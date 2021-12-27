package com.example.apossbackend.service;

import com.example.apossbackend.model.ProductsResponse;
import com.example.apossbackend.model.dto.*;

import java.util.List;


public interface ProductService {

    ProductsResponse getAllProduct(int pageNo, int pageSize, String sortBy, String sortDir);

    ProductsResponse getAllProductByKeyword(String keyword,int pageNo, int pageSize, String sortBy, String sortDir);

    ProductDetailDTO getProductDetail(long id);

    ProductsResponse getAllProductByKindId(long id, int pageNo, int pageSize);

    List<ProductImageDTO> getAllProductImageByProductId(long id);

    List<ProductRatingDTO> getAllProductRatingOfProductId(long id);

    List<ProductPropertyDTO> getAllPropertyOfProductId(long id, boolean isColor);

    long createNewProduct(NewProductDTO newProductDTO);

    void updateProductById(NewProductDTO newProductDTO, Long id);

    void deleteProductById(Long id);

    void createNewProductImage(NewProductImageDTO newImage);

    void updateProductImageById(NewProductImageDTO newImage, Long id);

    void deleteProductImageById(Long id);

    void createNewProductProperty(ProductPropertyDTO productPropertyDTO);
    void deleteProductPropertyById(long id);

    void createNewProductPropertyValue(ProductPropertyValueDTO productPropertyValueDTO, long propertyId);
    void deleteProductPropertyValueById(long id);
}
