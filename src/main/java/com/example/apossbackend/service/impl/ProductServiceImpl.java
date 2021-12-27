package com.example.apossbackend.service.impl;

import com.example.apossbackend.exception.ResourceNotFoundException;
import com.example.apossbackend.model.ProductsResponse;
import com.example.apossbackend.model.dto.*;
import com.example.apossbackend.model.entity.*;
import com.example.apossbackend.repository.*;
import com.example.apossbackend.service.ProductService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final ProductImageRepository productImageRepository;
    private final RatingRepository ratingRepository;
    private final ProductPropertyRepository productPropertyRepository;
    private final KindRepository kindRepository;


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

    @Override
    public long createNewProduct(NewProductDTO newProductDTO) {
        ProductEntity product = new ProductEntity();
        product.setName(newProductDTO.getName());
        product.setPrice(newProductDTO.getPrice());
        product.setDescription(newProductDTO.getDescription());
        product.setQuantity(newProductDTO.getQuantity());
        KindEntity kind = kindRepository.findById(newProductDTO.getKindId()).orElseThrow(
                () -> new ResourceNotFoundException("Kind","Id", newProductDTO.getKindId())
        );
        product.setKind(kind);
        product.setCreateTime(new Timestamp(new Date().getTime()));
        product.setUpdateTime(new Timestamp(new Date().getTime()));
        return productRepository.save(product).getId();
    }

    @Override
    public void updateProductById(NewProductDTO newProductDTO, Long id) {
        ProductEntity product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", id)
        );
        product.setName(newProductDTO.getName());
        product.setPrice(newProductDTO.getPrice());
        product.setQuantity(newProductDTO.getQuantity());
        product.setDescription(newProductDTO.getDescription());
        KindEntity kind = kindRepository.findById(newProductDTO.getKindId()).orElseThrow(
                () -> new ResourceNotFoundException("Kind","Id", newProductDTO.getKindId())
        );
        product.setKind(kind);
        product.setUpdateTime(new Timestamp(new Date().getTime()));
        productRepository.save(product);
    }
    @Override
    public void deleteProductById(Long id) {
        ProductEntity product = productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", id)
        );
        productRepository.deleteById(product.getId());
    }

    @Override
    public void createNewProductImage(NewProductImageDTO newImage) {
        ProductImageEntity productImage = new ProductImageEntity();
        productImage.setImageUrl(newImage.getImageUrl());
        productImage.setPriority(newImage.getPriority());
        ProductEntity product = productRepository.findById(newImage.getProductId()).orElseThrow(
                () -> new ResourceNotFoundException("Product", "id", newImage.getProductId())
        );
        productImage.setProduct(product);
        productImage.setCreateTime(new Timestamp(new Date().getTime()));
        productImage.setUpdateTime(new Timestamp(new Date().getTime()));
        productImageRepository.save(productImage);
    }

    @Override
    public void updateProductImageById(NewProductImageDTO newImage, Long id) {
        ProductImageEntity productImage = productImageRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Image", "id", id)
        );
        productImage.setImageUrl(newImage.getImageUrl());
        productImage.setPriority(newImage.getPriority());
        productImageRepository.save(productImage);
    }

    @Override
    public void deleteProductImageById(Long id) {
        productImageRepository.deleteById(id);
    }
}
