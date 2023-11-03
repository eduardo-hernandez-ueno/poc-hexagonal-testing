package com.example.infrastructure.mappers;

import com.example.infrastructure.controller.dtos.ProductRequestDto;
import com.example.products.domain.Product;
import org.springframework.stereotype.Component;


import java.lang.reflect.Method;
import java.util.function.Function;

@Component
public class ProductRequestToProduct implements Function<ProductRequestDto, Product> {
    @Override
    public Product apply(ProductRequestDto productRequestDto) {
        return new Product(productRequestDto.code(), productRequestDto.name(), productRequestDto.price());
    }
}
