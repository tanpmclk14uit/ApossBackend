package com.example.apossbackend.service.impl;

import com.example.apossbackend.model.ProductsResponse;
import com.example.apossbackend.model.dto.ProductDTO;
import com.example.apossbackend.model.entity.ProductEntity;
import com.example.apossbackend.repository.ProductRepository;
import com.example.apossbackend.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

}
