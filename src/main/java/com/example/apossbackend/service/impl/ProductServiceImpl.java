package com.example.apossbackend.service.impl;

import com.example.apossbackend.model.ProductResponse;
import com.example.apossbackend.model.dto.ProductDTO;
import com.example.apossbackend.model.entity.ProductEntity;
import com.example.apossbackend.repository.ProductRepository;
import com.example.apossbackend.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        super();
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
    }

    private ProductDTO mapToProductDTO(ProductEntity productEntity) {
        return modelMapper.map(productEntity, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProduct(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<ProductEntity> productsPage = productRepository.findAll(pageable);
        List<ProductEntity> productEntityList = productsPage.getContent();
        List<ProductDTO> content = productEntityList.stream().map(this::mapToProductDTO).collect(Collectors.toList());
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(content);
        productResponse.setLast(productsPage.isLast());
        productResponse.setTotalElements(productsPage.getTotalElements());
        productResponse.setPageNo(productsPage.getNumber());
        productResponse.setPageSize(productsPage.getSize());
        productResponse.setTotalPages(productsPage.getTotalPages());
        return productResponse;
    }
}
