package com.example.apossbackend.service.impl;

import com.example.apossbackend.model.dto.KindDTO;
import com.example.apossbackend.model.dto.ProductDTO;
import com.example.apossbackend.model.entity.KindEntity;
import com.example.apossbackend.model.entity.ProductEntity;
import com.example.apossbackend.repository.KindRepository;
import com.example.apossbackend.service.KindService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KindServiceImpl implements KindService {

    private final KindRepository kindRepository;
    private final ModelMapper modelMapper;

    public KindServiceImpl(KindRepository kindRepository, ModelMapper modelMapper) {
        this.kindRepository = kindRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<KindDTO> getAllKind() {
        List<KindEntity> kindEntities = kindRepository.findAll();
        return kindEntities.stream().map(this::mapToKindDTO).collect(Collectors.toList());
    }

    @Override
    public List<KindDTO> getAllKindByCategoryId(Long categoryId) {
        List<KindEntity> kindEntities = kindRepository.findKindEntitiesByIndustryId(categoryId);
        return kindEntities.stream().map(this::mapToKindDTO).collect(Collectors.toList());
    }

    private KindDTO mapToKindDTO(KindEntity kindEntity) {
        KindDTO kindDTO = new KindDTO();
        List<ProductDTO> productDTOList = kindEntity.getProducts().stream().map(this::mapToProductDTO).collect(Collectors.toList());
        kindDTO.setId(kindEntity.getId());
        kindDTO.setName(kindEntity.getName());
        kindDTO.setProducts(productDTOList);
        kindDTO.setRating(kindEntity.getRating());
        kindDTO.setTotalProducts(kindEntity.getTotalProduct());
        kindDTO.setTotalPurchases(kindEntity.getPurchased());
        kindDTO.setCategory(kindEntity.getIndustry().getId());
        kindDTO.setImage(kindEntity.getImage());
        return kindDTO;
    }

    private ProductDTO mapToProductDTO(ProductEntity productEntity) {
        ProductDTO product = new ProductDTO();
        product = modelMapper.map(productEntity, ProductDTO.class);
        product.setImage("https://s.yimg.com/os/creatr-uploaded-images/2020-11/c891d158-28a0-11eb-afc3-f454cc2e3b45");
        return product;
    }
}
