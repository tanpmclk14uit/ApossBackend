package com.example.apossbackend.controller;

import com.example.apossbackend.model.ProductsResponse;
import com.example.apossbackend.model.dto.ProductDetailDTO;
import com.example.apossbackend.model.dto.ProductImageDTO;
import com.example.apossbackend.model.dto.ProductPropertyDTO;
import com.example.apossbackend.model.dto.ProductRatingDTO;
import com.example.apossbackend.service.ProductService;
import com.example.apossbackend.utils.AppConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        super();
        this.productService = productService;
    }

    @GetMapping
    public ProductsResponse getAllPosts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return productService.getAllProduct(pageNo, pageSize, sortBy,sortDir);
    }
    @GetMapping("/search")
    public ProductsResponse getAllPostsByKeyword(
            @RequestParam(value = "keyword", defaultValue = AppConstants.DEFAULT_KEYWORD, required = false) String keyword,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ){
        return productService.getAllProductByKeyword(keyword, pageNo, pageSize, sortBy,sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDTO> getProductDetailById(
            @PathVariable (value = "id") long id
    ){
        return ResponseEntity.ok(productService.getProductDetail(id));
    }

    @GetMapping("/kind/{id}")
    public ProductsResponse getAllProductByKindId(
            @PathVariable(value =  "id") long id,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize
    ){
        return productService.getAllProductByKindId(id, pageNo, pageSize);
    }

    @GetMapping("/{id}/images")
    public ResponseEntity<List<ProductImageDTO>> getAllProductImageByProductId(
            @PathVariable(value = "id") long id
    ){
        return ResponseEntity.ok(productService.getAllProductImageByProductId(id));
    }
    @GetMapping("/{id}/ratings")
    public ResponseEntity<List<ProductRatingDTO>> getAllProductRatingOfProductId(
            @PathVariable(value = "id") long id
    ){
        return ResponseEntity.ok(productService.getAllProductRatingOfProductId(id));
    }
    @GetMapping("/{id}/properties")
    public ResponseEntity<List<ProductPropertyDTO>> getAllProductPropertyById(
            @PathVariable(value = "id") long id,
            @RequestParam(value = "isColor", defaultValue = "false", required = false) boolean isColor
    ){
        return ResponseEntity.ok(productService.getAllPropertyOfProductId(id, isColor));
    }

}
