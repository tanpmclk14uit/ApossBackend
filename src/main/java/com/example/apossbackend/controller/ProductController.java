package com.example.apossbackend.controller;

import com.example.apossbackend.model.ProductsResponse;
import com.example.apossbackend.model.dto.*;
import com.example.apossbackend.service.ProductService;
import com.example.apossbackend.utils.AppConstants;
import org.springframework.context.annotation.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ProductsResponse getAllProducts(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return productService.getAllProduct(pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/search")
    public ProductsResponse getAllProductsByKeyword(
            @RequestParam(value = "keyword", defaultValue = AppConstants.DEFAULT_KEYWORD, required = false) String keyword,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return productService.getAllProductByKeyword(keyword, pageNo, pageSize, sortBy, sortDir);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailDTO> getProductDetailById(
            @PathVariable(value = "id") long id
    ) {
        return ResponseEntity.ok(productService.getProductDetail(id));
    }

    @GetMapping("/kind/{id}")
    public ProductsResponse getAllProductByKindId(
            @PathVariable(value = "id") long id,
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize
    ) {
        return productService.getAllProductByKindId(id, pageNo, pageSize);
    }

    @GetMapping("/{id}/images")
    public ResponseEntity<List<ProductImageDTO>> getAllProductImageByProductId(
            @PathVariable(value = "id") long id
    ) {
        return ResponseEntity.ok(productService.getAllProductImageByProductId(id));
    }
    @PostMapping("/image")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addNewImage(
            @RequestBody NewProductImageDTO newProductImageDTO
    ){
        productService.createNewProductImage(newProductImageDTO);
        return ResponseEntity.ok("Add new product image success");
    }
    @PutMapping("/image/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateProductImage(
            @RequestBody NewProductImageDTO newProductImageDTO,
            @PathVariable(value = "id") long id
    ){
        productService.updateProductImageById(newProductImageDTO, id);
        return ResponseEntity.ok("Update product image success");
    }
    @DeleteMapping("/image/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProductImage(
            @PathVariable(value = "id") long id
    ){
        productService.deleteProductImageById(id);
        return ResponseEntity.ok("Remove product image success");
    }

    @GetMapping("/{id}/ratings")
    public ResponseEntity<List<ProductRatingDTO>> getAllProductRatingOfProductId(
            @PathVariable(value = "id") long id
    ) {
        return ResponseEntity.ok(productService.getAllProductRatingOfProductId(id));
    }

    @GetMapping("/{id}/properties")
    public ResponseEntity<List<ProductPropertyDTO>> getAllProductPropertyById(
            @PathVariable(value = "id") long id,
            @RequestParam(value = "isColor", defaultValue = "false", required = false) boolean isColor
    ) {
        return ResponseEntity.ok(productService.getAllPropertyOfProductId(id, isColor));
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> createNewProduct(
            @RequestBody NewProductDTO newProductDTO
    ) {
        return ResponseEntity.ok(productService.createNewProduct(newProductDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateProduct(
            @RequestBody NewProductDTO newProductDTO,
            @PathVariable("id") long id
    ) {
        productService.updateProductById(newProductDTO, id);
        return ResponseEntity.ok("Update product success");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProduct(
            @PathVariable("id") long id
    ) {
        productService.deleteProductById(id);
        return ResponseEntity.ok("Remove product success");
    }

    @PostMapping("/property")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createNewProperty(
            @RequestBody ProductPropertyDTO productPropertyDTO
    ){
        productService.createNewProductProperty(productPropertyDTO);
        return ResponseEntity.ok("Create new property success");
    }
    @DeleteMapping("/property/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteProperty(
            @PathVariable("id") long id
    ){
        productService.deleteProductPropertyById(id);
        return ResponseEntity.ok("Remove product property success");
    }

    @PostMapping("/property/{id}/value")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createNewPropertyValue(
            @RequestBody ProductPropertyValueDTO productPropertyValueDTO,
            @PathVariable("id") long propertyId
    ){
        productService.createNewProductPropertyValue(productPropertyValueDTO,propertyId);
        return ResponseEntity.ok("Create new property value success");
    }

    @DeleteMapping("/property/value/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePropertyValue(
            @PathVariable("id") long id
    ){
        productService.deleteProductPropertyValueById(id);
        return ResponseEntity.ok("Remove product property value success");
    }
    @PostMapping("/{id}/property-value")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> applyPropertyValueForProduct(
            @RequestBody ProductPropertyValueDTO productPropertyValueDTO,
            @PathVariable("id") long productId
    ){
        productService.applyPropertyValueForProduct(productPropertyValueDTO, productId);
        return ResponseEntity.ok("Apply property value for product success");
    }

    @PutMapping("/{id}/property-value")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> editPropertyValueForProduct(
            @RequestBody ProductPropertyValueDTO productPropertyValueDTO,
            @PathVariable("id") long productId
    ){
        productService.updatePropertyValueForProduct(productPropertyValueDTO, productId);
        return ResponseEntity.ok("Update property value for product success");
    }
    @DeleteMapping("/{id}/property-value/{valueId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePropertyValue(
            @PathVariable("id") long productId,
            @PathVariable("valueId") long valueId
    ){
        productService.removePropertyValueOfProduct(valueId, productId);
        return ResponseEntity.ok("Remove product property value success");
    }



}
