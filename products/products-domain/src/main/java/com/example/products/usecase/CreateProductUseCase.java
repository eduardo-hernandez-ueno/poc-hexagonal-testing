package com.example.products.usecase;

import com.example.products.domain.Product;

public interface CreateProductUseCase {
    Product execute(Product product);
}
