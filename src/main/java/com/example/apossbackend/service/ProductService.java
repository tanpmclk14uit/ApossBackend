package com.example.apossbackend.service;

import com.example.apossbackend.model.ProductsResponse;
import com.example.apossbackend.model.dto.*;
import com.example.apossbackend.model.dto.*;
import com.example.apossbackend.model.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestBody;

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

    int getQuantityByValueIds(List<Long> valueIds, long productId);

    int getAdditionalPriceByValueIds(List<Long> valueIds, long productId);

    long getSetIdByValuesIds(List<Long> valueIds, long productId);

    long createNewProduct(NewProductDTO newProductDTO);

    long createNewProductWithDefaultSet(NewProductDTO newProductDTO);

    void updateProductById(NewProductDTO newProductDTO, Long id);

    void deleteProductById(Long id);

    void createNewProductImage(NewProductImageDTO newImage);

    void updateProductImageById(NewProductImageDTO newImage, Long id);

    void deleteProductImageById(Long id);

    void createNewProductProperty(ProductPropertyDTO productPropertyDTO);
    void deleteProductPropertyById(long id);

    void createNewProductPropertyValue(ProductPropertyValueDTO productPropertyValueDTO, long propertyId);
    void deleteProductPropertyValueById(long id);

    void createNewSetForProduct(SetDTO setDTO, long productId);
//    void removePropertyValueOfProduct(SetDTO setDTO);
//    void updatePropertyValueForProduct(SetDTO setDTO);
}
