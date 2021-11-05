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

    public KindServiceImpl(KindRepository kindRepository, ModelMapper modelMapper){
        this.kindRepository = kindRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<KindDTO> getAllKind() {
        List<KindEntity> kindEntityList = kindRepository.findAll();
        List<KindDTO> kindDTOList = new ArrayList<KindDTO>();
        for (int i =0; i<kindEntityList.size() ; i++) {
            KindEntity kindEntity = kindEntityList.get(i);
            KindDTO kindDTO = new KindDTO();
            List<ProductDTO> productDTOList = kindEntity.getProducts().stream().map(this::mapToProductDTO).collect(Collectors.toList());
            kindDTO.setId(kindEntity.getId());
            kindDTO.setName(kindEntity.getName());
            kindDTO.setProducts(productDTOList);
            kindDTO.setRating(kindEntity.getRating());
            kindDTO.setTotalProducts(kindEntity.getTotalProduct());
            kindDTO.setTotalPurchases(kindEntity.getPurchased());
            kindDTOList.add(kindDTO);
        }
        return kindDTOList;
    }

    private ProductDTO mapToProductDTO(ProductEntity productEntity) {
        return modelMapper.map(productEntity, ProductDTO.class);
    }
}
