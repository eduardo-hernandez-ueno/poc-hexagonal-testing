package com.example.infrastructure.mappers;

import com.example.infrastructure.controller.dtos.ProductDto;
import com.example.products.domain.Product;
import com.example.products.domain.Tag;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ProductToProductDto implements Function<Product, ProductDto> {
    @Override
    public ProductDto apply(Product product) {
        return new ProductDto(product.code(), product.name(), product.price(), product.tags().stream()
                .collect(Collectors.toMap(Tag::name, Tag::value)));
    }
}
