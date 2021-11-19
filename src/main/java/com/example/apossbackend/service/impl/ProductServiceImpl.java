package com.example.apossbackend.service.impl;

import com.example.apossbackend.exception.ApossBackendException;
import com.example.apossbackend.exception.ResourceNotFoundException;
import com.example.apossbackend.model.ProductsResponse;
import com.example.apossbackend.model.dto.ProductDTO;
import com.example.apossbackend.model.dto.ProductDetailDTO;
import com.example.apossbackend.model.dto.ProductImageDTO;
import com.example.apossbackend.model.entity.ProductEntity;
import com.example.apossbackend.model.entity.ProductImageEntity;
import com.example.apossbackend.repository.ProductImageRepository;
import com.example.apossbackend.repository.ProductRepository;
import com.example.apossbackend.repository.RatingRepository;
import com.example.apossbackend.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public ProductsResponse getAllProduct(int pageNo, int pageSize, String sortBy, String sortDir) {
        //Sort
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        //Pageable
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize,sort);

        Page<ProductEntity> productsPage = productRepository.findAll(pageable);
        return getProductsResponse(productsPage);
    }

    @Override
    public ProductsResponse getAllProductByKeyword(String keyword,int pageNo, int pageSize, String sortBy, String sortDir) {
        //Sort
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        //Pageable
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize,sort);
        Page<ProductEntity> productsPage = productRepository.findAllByNameContains(keyword,pageable);
        return getProductsResponse(productsPage);
    }

    @Override
    public ProductDetailDTO getProductDetail(long id) {
        ProductEntity product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", id)
        );
        return mapToProductDetailDTO(product);
    }

    private ProductsResponse getProductsResponse(Page<ProductEntity> productsPage) {
        List<ProductEntity> productEntityList = productsPage.getContent();
        List<ProductDTO> content = productEntityList.stream().map(this::mapToProductDTO).collect( Collectors.toList());
        ProductsResponse productsResponse = new ProductsResponse();
        productsResponse.setContent(content);
        productsResponse.setLast(productsPage.isLast());
        productsResponse.setTotalElements(productsPage.getTotalElements());
        productsResponse.setPageNo(productsPage.getNumber());
        productsResponse.setPageSize(productsPage.getSize());
        productsResponse.setTotalPages(productsPage.getTotalPages());
        return productsResponse;
    }

    @Override
    public ProductsResponse getAllProductByKindId(long id, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Page<ProductEntity> productsPage = productRepository.findProductEntityByKindId(id, pageable);
        return getProductsResponse(productsPage);
    }

    @Override
    public List<ProductImageDTO> getAllProductImageByProductId(long id) {
        ProductEntity product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", id)
        );
        return product.getProductImages().stream().map(this::mapToProductImageDTO).collect(Collectors.toList());
    }

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final ProductImageRepository productImageRepository;
    private final RatingRepository ratingRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, ProductImageRepository productImageRepository, RatingRepository ratingRepository) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.productImageRepository = productImageRepository;
        this.ratingRepository = ratingRepository;
    }

    private ProductDTO mapToProductDTO(ProductEntity productEntity) {
        ProductDTO product = modelMapper.map(productEntity, ProductDTO.class);
        String imgURl = productImageRepository.findProductImageEntityByProductAndPriority(productEntity, 1).getImageUrl();
        product.setImage(imgURl);
        return product;
    }
    private ProductDetailDTO mapToProductDetailDTO(ProductEntity productEntity){
        ProductDetailDTO productDetailDTO = new ProductDetailDTO();
        productDetailDTO.setName(productEntity.getName());
        productDetailDTO.setPrice(productEntity.getPrice());
        productDetailDTO.setPurchase(productEntity.getPurchased());
        productDetailDTO.setRating(productEntity.getRating());
        productDetailDTO.setDescription(productEntity.getDescription());
        productDetailDTO.setKindId(productEntity.getKind().getId());
        productDetailDTO.setKindName(productEntity.getKind().getName());
        productDetailDTO.setQuantity(productEntity.getQuantity());
        int totalReview = ratingRepository.countAllByProductId(productEntity.getId());
        productDetailDTO.setTotalReview(totalReview);
        return productDetailDTO;
    }
    private ProductImageDTO mapToProductImageDTO(ProductImageEntity productImageEntity){
        return modelMapper.map(productImageEntity, ProductImageDTO.class);
    }

}
