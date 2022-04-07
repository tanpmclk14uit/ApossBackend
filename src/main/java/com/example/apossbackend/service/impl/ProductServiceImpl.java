package com.example.apossbackend.service.impl;

import com.example.apossbackend.exception.ApossBackendException;
import com.example.apossbackend.exception.ResourceNotFoundException;
import com.example.apossbackend.model.ProductsResponse;
import com.example.apossbackend.model.dto.*;
import com.example.apossbackend.model.entity.*;
import com.example.apossbackend.repository.*;
import com.example.apossbackend.service.ProductService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.PropertyValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
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
    private final ClassifyProductRepository classifyProductRepository;
    private final ClassifyProductValueRepository classifyProductValueRepository;
    private final SetValueRepository setValueRepository;


    private ProductRatingDTO mapToProductRatingDTO(RatingEntity rating) {
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

    private ProductDetailDTO mapToProductDetailDTO(ProductEntity productEntity) {
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

    private ProductImageDTO mapToProductImageDTO(ProductImageEntity productImageEntity) {
        return modelMapper.map(productImageEntity, ProductImageDTO.class);
    }

    @Override
    public ProductsResponse getAllProduct(int pageNo, int pageSize, String sortBy, String sortDir) {
        //Sort
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        //Pageable
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        Page<ProductEntity> productsPage = productRepository.findAll(pageable);
        return getProductsResponse(productsPage);
    }

    @Override
    public ProductsResponse getAllProductByKeyword(String keyword, int pageNo, int pageSize, String sortBy, String sortDir) {
        //Sort
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        //Pageable
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<ProductEntity> productsPage = productRepository.findAllByNameContains(keyword, pageable);
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
        List<ProductDTO> content = productEntityList.stream().map(this::mapToProductDTO).collect(Collectors.toList());
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
        Pageable pageable = PageRequest.of(0, product.getProductImages().size(), sort);
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
        List<ProductPropertyDTO> productPropertyDTOS = productPropertyRepository.findProductPropertyIdByProductId(id, isColor);
        for (ProductPropertyDTO productPropertyDTO : productPropertyDTOS) {
            List<ClassifyProductValueEntity> classifyProductValueEntities = classifyProductValueRepository.findClassifyProductValueEntitiesByClassifyProductId(productPropertyDTO.getId()).orElseThrow(
                    () -> new ResourceNotFoundException("Property value", "Property Id", productPropertyDTO.getId())
            );
            List<ProductPropertyValueDTO> classifyProductValueDTOS = classifyProductValueEntities.stream().map(this::mapClassifyProductValueEntityToProductPropertyValueDTO).collect(Collectors.toList());
            productPropertyDTO.setValueDTOS(classifyProductValueDTOS);
        }
        return productPropertyDTOS;
    }

    private ProductPropertyValueDTO mapClassifyProductValueEntityToProductPropertyValueDTO(ClassifyProductValueEntity classifyProductValue) {
        ProductPropertyValueDTO productPropertyValueDTO = new ProductPropertyValueDTO();
        productPropertyValueDTO.setValue(classifyProductValue.getValue());
        productPropertyValueDTO.setName(classifyProductValue.getName());
        productPropertyValueDTO.setId(classifyProductValue.getId());
        return productPropertyValueDTO;
    }

    @Override
    public long createNewProduct(NewProductDTO newProductDTO) {
        ProductEntity product = new ProductEntity();
        product.setName(newProductDTO.getName());
        product.setPrice(newProductDTO.getPrice());
        product.setDescription(newProductDTO.getDescription());
        product.setQuantity(newProductDTO.getQuantity());
        KindEntity kind = kindRepository.findById(newProductDTO.getKindId()).orElseThrow(
                () -> new ResourceNotFoundException("Kind", "Id", newProductDTO.getKindId())
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
                () -> new ResourceNotFoundException("Kind", "Id", newProductDTO.getKindId())
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

    @Override
    public void createNewProductProperty(ProductPropertyDTO productPropertyDTO) {
        ClassifyProductEntity classifyProductEntity = new ClassifyProductEntity();
        classifyProductEntity.setName(productPropertyDTO.getName());
        classifyProductEntity.setPropertyColor(productPropertyDTO.isColor());
        classifyProductEntity.setCreateTime(new Timestamp(new Date().getTime()));
        classifyProductEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        classifyProductRepository.save(classifyProductEntity);
    }

    @Override
    public void deleteProductPropertyById(long id) {
        classifyProductRepository.deleteById(id);
    }

    @Override
    public void createNewProductPropertyValue(ProductPropertyValueDTO productPropertyValueDTO, long propertyId) {
        ClassifyProductValueEntity classifyProductValueEntity = new ClassifyProductValueEntity();
        classifyProductValueEntity.setName(productPropertyValueDTO.getName());
        classifyProductValueEntity.setValue(productPropertyValueDTO.getValue());
        ClassifyProductEntity classifyProductEntity = classifyProductRepository.findById(propertyId).orElseThrow(
                () -> new ResourceNotFoundException("Classify product", "id", propertyId)
        );
        classifyProductValueEntity.setClassifyProduct(classifyProductEntity);
        classifyProductValueEntity.setCreateTime(new Timestamp(new Date().getTime()));
        classifyProductValueEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        classifyProductValueRepository.save(classifyProductValueEntity);
    }

    @Override
    public void deleteProductPropertyValueById(long id) {
        classifyProductValueRepository.deleteById(id);
    }

    @Override
    public void createNewSetForProduct(SetDTO setDTO, long productId) {
        SetEntity setEntity = new SetEntity();
        ProductEntity product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "Id", productId)
        );
        setEntity.setAdditionalPrice(setDTO.getAdditionalPrice());
        setEntity.setQuantity(setDTO.getQuantity());
        setEntity.setProduct(product);
        setEntity = productPropertyRepository.saveAndFlush(setEntity);
        for (long id : setDTO.getValueIds()) {
            createNewSetValueEntity(setEntity, id);
        }
    }

    private void createNewSetValueEntity(SetEntity setEntity, long propertyValueId) {
        ClassifyProductValueEntity classifyProductValueEntity = classifyProductValueRepository.findById(propertyValueId).orElseThrow(
                () -> new ResourceNotFoundException("Product property value", "Id", propertyValueId)
        );
        SetValueEntity setValueEntity = new SetValueEntity();
        setValueEntity.setSet(setEntity);
        setValueEntity.setClassifyProductValue(classifyProductValueEntity);
        setValueRepository.save(setValueEntity);
    }


    //    @Override
//    public void updatePropertyValueForProduct(SetDTO setDTO) {
//        ProductEntity product = productRepository.findById(setDTO.getProductId()).orElseThrow(
//                () -> new ResourceNotFoundException("Product", "Id", setDTO.getProductId())
//        );
//        List<SetValueEntity> setValueEntityList = new ArrayList<>();
//        for ( SetValueDTO setValueDTO: setDTO.getSetValueDTOList()) {
//            ClassifyProductValueEntity classifyProductValueEntity = classifyProductValueRepository.findById(setValueDTO.getPropertyValueId()).orElseThrow(
//                    () -> new ResourceNotFoundException("Product property value", "Id", setValueDTO.getPropertyValueId())
//            );
//            SetValueEntity setValueEntity = convertSetValueDTOToEntity(setValueDTO, classifyProductValueEntity);
//            setValueEntityList.add(setValueEntity);
//        }
//
//        SetEntity setEntity = convertSetDTOToEntity(setDTO, product, setValueEntityList);
//        productPropertyRepository.save(setEntity);
//    }
//
//    @Override
//    public void removePropertyValueOfProduct(SetDTO setDTO) {
//        ProductEntity product = productRepository.findById(setDTO.getProductId()).orElseThrow(
//                () -> new ResourceNotFoundException("Product", "Id", setDTO.getProductId())
//        );
//        for ( SetValueDTO setValueDTO: setDTO.getSetValueDTOList()) {
//            ClassifyProductValueEntity classifyProductValueEntity = classifyProductValueRepository.findById(setValueDTO.getPropertyValueId()).orElseThrow(
//                    () -> new ResourceNotFoundException("Product property value", "Id", setValueDTO.getPropertyValueId())
//            );
//        }
//        SetEntity setEntity = productPropertyRepository.findById(setDTO.getSetId()).orElseThrow(
//                () -> new ResourceNotFoundException("Set", "Id", setDTO.getSetId())
//        );
//        productPropertyRepository.delete(setEntity);
//    }

    private ClassifyProductValueEntity convertProductPropertyValueDTOToClassifyProductValueEntity(ProductPropertyValueDTO productPropertyValueDTO, ProductPropertyDTO productPropertyDTO) {
        ClassifyProductValueEntity classifyProductValueEntity = new ClassifyProductValueEntity();
        classifyProductValueEntity.setValue(productPropertyValueDTO.getValue());
        classifyProductValueEntity.setName(productPropertyValueDTO.getName());
        classifyProductValueEntity.setClassifyProduct(classifyProductRepository.findClassifyProductEntityById(productPropertyDTO.getId()));
        return classifyProductValueEntity;
    }

    private SetEntity convertSetDTOToEntity(SetDTO setDTO, ProductEntity productEntity, List<SetValueEntity> setValueEntityList) {
        SetEntity setEntity = new SetEntity();
        setEntity.setProduct(productEntity);
        setEntity.setSetValueEntity(setValueEntityList);
        setEntity.setQuantity(setDTO.getQuantity());
        setEntity.setAdditionalPrice(setDTO.getAdditionalPrice());
        setEntity.setCreateTime(new Timestamp(new Date().getTime()));
        setEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        for (SetValueEntity setValue :
                setEntity.getSetValueEntity()) {
            setValue.setSet(setEntity);
        }
        return setEntity;
    }

    private SetValueEntity convertSetValueDTOToEntity(SetValueDTO setValueDTO, ClassifyProductValueEntity classifyProductValueEntity) {
        SetValueEntity setValueEntity = new SetValueEntity();
        setValueEntity.setClassifyProductValue(classifyProductValueEntity);
        setValueEntity.setCreateTime(new Timestamp(new Date().getTime()));
        setValueEntity.setUpdateTime(new Timestamp(new Date().getTime()));
        return setValueEntity;
    }
}
