package com.example.apossbackend.service.impl;

import com.example.apossbackend.exception.ResourceNotFoundException;
import com.example.apossbackend.model.ProductsResponse;
import com.example.apossbackend.model.dto.*;
import com.example.apossbackend.model.entity.*;
import com.example.apossbackend.repository.ProductImageRepository;
import com.example.apossbackend.repository.ProductPropertyRepository;
import com.example.apossbackend.repository.ProductRepository;
import com.example.apossbackend.repository.RatingRepository;
import com.example.apossbackend.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
        Sort sort = Sort.by("priority").ascending();
        //Pageable
        Pageable pageable = PageRequest.of(0, product.getProductImages().size(),sort);
        Page<ProductImageEntity> productEntities = productImageRepository.findProductImageEntitiesByProductId(id, pageable);
        List<ProductImageEntity> productImageEntities = productEntities.getContent();
        return productImageEntities.stream().map(this::mapToProductImageDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProductRatingDTO> getAllProductRatingOfProductId(long id) {
        ProductEntity product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", id)
        );
        return product.getProductRatings().stream().map(this::mapToProductRatingDTO).collect(Collectors.toList());
    }

    @Override
    public List<ProductPropertyDTO> getAllPropertyOfProductId(long id, boolean isColor) {
        List<ProductPropertyDTO> productPropertyDTOS =productPropertyRepository.findProductPropertyIdByProductId(id, isColor);
        List<ProductToClassifyProductValueEntity> productToClassifyProductValueEntities = productPropertyRepository.findProductToClassifyProductValueEntitiesByProductId(id);
        for (ProductPropertyDTO productPropertyDTO : productPropertyDTOS) {
            ArrayList<ProductPropertyValueDTO> productPropertyValueDTOS = new ArrayList<>();
            for (ProductToClassifyProductValueEntity productToClassifyProductValueEntity : productToClassifyProductValueEntities) {
                if (productPropertyDTO.getId() == productToClassifyProductValueEntity.getClassifyProductValue().getClassifyProduct().getId()) {
                    productPropertyValueDTOS.add(mapToProductPropertyValueDTO(productToClassifyProductValueEntity));
                }
            }
            productPropertyDTO.setValueDTOS(productPropertyValueDTOS);
        }
        return productPropertyDTOS;
    }

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final ProductImageRepository productImageRepository;
    private final RatingRepository ratingRepository;
    private final ProductPropertyRepository productPropertyRepository;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper, ProductImageRepository productImageRepository, RatingRepository ratingRepository, ProductPropertyRepository productPropertyRepository) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.productImageRepository = productImageRepository;
        this.ratingRepository = ratingRepository;
        this.productPropertyRepository = productPropertyRepository;
    }

    private ProductRatingDTO mapToProductRatingDTO(RatingEntity rating){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        ProductRatingDTO productRatingDTO = new ProductRatingDTO();
        productRatingDTO.setId(rating.getId());
        productRatingDTO.setName(rating.getCustomer().getName());
        productRatingDTO.setRating(rating.getRating());
        productRatingDTO.setTime(simpleDateFormat.format(rating.getCreateTime()));
        productRatingDTO.setContent(rating.getComment());
        productRatingDTO.setImageAvatarURl(rating.getCustomer().getImage());
        productRatingDTO.setListImageURL(rating.getRatingImages().stream().map(RatingImageEntity::getImageUrl).collect(Collectors.toList()));
        return productRatingDTO;
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
    private ProductPropertyValueDTO mapToProductPropertyValueDTO(ProductToClassifyProductValueEntity productValue){
        ProductPropertyValueDTO productPropertyValueDTO = new ProductPropertyValueDTO();
        productPropertyValueDTO.setId(productValue.getId());
        productPropertyValueDTO.setName(productValue.getClassifyProductValue().getName());
        productPropertyValueDTO.setQuantity(productValue.getQuantity());
        productPropertyValueDTO.setAdditionalPrice(productValue.getAdditionalPrice());
        productPropertyValueDTO.setValue(productValue.getClassifyProductValue().getValue());
        return productPropertyValueDTO;
    }



}
