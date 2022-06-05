package com.example.apossbackend.service.impl;

import com.example.apossbackend.exception.ResourceNotFoundException;
import com.example.apossbackend.model.dto.KindDTO;
import com.example.apossbackend.model.dto.ProductDTO;
import com.example.apossbackend.model.entity.IndustryEntity;
import com.example.apossbackend.model.entity.KindEntity;
import com.example.apossbackend.model.entity.ProductEntity;
import com.example.apossbackend.repository.IndustryRepository;
import com.example.apossbackend.repository.KindRepository;
import com.example.apossbackend.repository.ProductImageRepository;
import com.example.apossbackend.repository.ProductRepository;
import com.example.apossbackend.service.KindService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KindServiceImpl implements KindService {

    private final KindRepository kindRepository;
    private final IndustryRepository industryRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public KindServiceImpl(KindRepository kindRepository, ModelMapper modelMapper, IndustryRepository industryRepository, ProductImageRepository productImageRepository, ProductRepository productRepository) {
        this.kindRepository = kindRepository;
        this.modelMapper = modelMapper;
        this.industryRepository = industryRepository;
        this.productImageRepository = productImageRepository;
        this.productRepository = productRepository;
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

    @Override
    public void deleteKind(long id) {
        if (kindRepository.existsById(id)) {
            kindRepository.deleteById(id);
        } else {
            throw  new ResourceNotFoundException("Kind", "id", id);
        }
    }

    @Override
    public void addKind(KindDTO kindDTO, long industryId) {
        IndustryEntity industryEntity = industryRepository.findById(industryId).orElseThrow(
                () -> new ResourceNotFoundException("Industry", "id", industryId)
        );
        kindRepository.save(mapToKindEntity(kindDTO, industryEntity));
    }

    @Override
    public void updateKind(KindDTO kindDTO, long id) {
        KindEntity kindEntity = kindRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Kind", "id", id)
        );
        kindEntity.setName(kindDTO.getName());
        kindEntity.setImage(kindDTO.getImage());
        kindRepository.save(kindEntity);
    }

    private KindDTO mapToKindDTO(KindEntity kindEntity) {
        KindDTO kindDTO = new KindDTO();
        List<ProductDTO> productDTOList = kindEntity.getProducts().stream().map(this::mapToProductDTO).collect(Collectors.toList());
        kindDTO.setId(kindEntity.getId());
        kindDTO.setName(kindEntity.getName());
        kindDTO.setProducts(productDTOList);
        kindDTO.setTotalProducts(kindEntity.getTotalProduct());
        kindDTO.setCategory(kindEntity.getIndustry().getId());
        kindDTO.setImage(kindEntity.getImage());
        return kindDTO;
    }

    private KindEntity mapToKindEntity(KindDTO kindDTO, IndustryEntity industry){
        KindEntity kindEntity = new KindEntity();
        kindEntity.setImage(kindDTO.getImage());
        kindEntity.setIndustry(industry);
        kindEntity.setProducts(kindDTO.getProducts().stream().map(
                productDTO -> {
                    ProductEntity productEntity = productRepository.findById(productDTO.getId()).orElseThrow(
                            () -> new ResourceNotFoundException("Product", "Id", productDTO.getId())
                    );
                    return  productEntity;
                }
        ).collect(Collectors.toList()));
        kindEntity.setTotalProduct(kindDTO.getTotalProducts());
        return  kindEntity;
    }


    private ProductDTO mapToProductDTO(ProductEntity productEntity) {
        ProductDTO product = new ProductDTO();
        product = modelMapper.map(productEntity, ProductDTO.class);
        product.setImage("https://s.yimg.com/os/creatr-uploaded-images/2020-11/c891d158-28a0-11eb-afc3-f454cc2e3b45");
        return product;
    }
}
