package com.example.infrastructure.controller;

import com.example.infrastructure.adapters.inbound.CreateProductHandler;
import com.example.infrastructure.controller.dtos.ProductDto;
import com.example.infrastructure.adapters.inbound.FindProductByCodeHandler;
import com.example.infrastructure.controller.dtos.ProductRequestDto;
import com.example.products.exceptions.ProductAlreadyExistsException;
import com.example.products.exceptions.ProductNotExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final FindProductByCodeHandler<ProductDto> findProductByCodeHandler;
    private final CreateProductHandler<ProductRequestDto, ProductDto> createProductHandler;

    public ProductController(FindProductByCodeHandler<ProductDto> findProductByCodeHandler, CreateProductHandler<ProductRequestDto, ProductDto> createProductHandler) {
        this.findProductByCodeHandler = findProductByCodeHandler;
        this.createProductHandler = createProductHandler;
    }

    @GetMapping("/{code}")
    public ResponseEntity<ProductDto> findByCode(@PathVariable String code) {
        try {
            return ResponseEntity.ok(findProductByCodeHandler.apply(code));
        } catch (ProductNotExistsException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody ProductRequestDto productDto) {
        try {
            return ResponseEntity.status(201).body(createProductHandler.apply(productDto));
        } catch (ProductAlreadyExistsException ex) {
            return ResponseEntity.status(409).build();
        }
    }
}
