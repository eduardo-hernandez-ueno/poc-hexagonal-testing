package com.example.infrastructure.config;

import com.example.infrastructure.adapters.inbound.CreateProductHandler;
import com.example.infrastructure.controller.dtos.ProductDto;
import com.example.infrastructure.adapters.inbound.FindProductByCodeHandler;
import com.example.infrastructure.controller.dtos.ProductRequestDto;
import com.example.infrastructure.mappers.ProductRequestToProduct;
import com.example.infrastructure.mappers.ProductToProductDto;
import com.example.products.usecase.CreateProductUseCase;
import com.example.products.usecase.FindProductByCodeUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HandlerConfig {
    @Bean
    public FindProductByCodeHandler<ProductDto> findProductByCodeHandler(FindProductByCodeUseCase findProductByCodeUseCase, ProductToProductDto mapper) {
        return new FindProductByCodeHandler<>(findProductByCodeUseCase, mapper);
    }

    @Bean
    public CreateProductHandler<ProductRequestDto, ProductDto> createProductHandler(CreateProductUseCase createProductUseCase, ProductRequestToProduct mapperToDomain, ProductToProductDto mapperToResponse) {
        return new CreateProductHandler<>(createProductUseCase, mapperToDomain, mapperToResponse);
    }
}
