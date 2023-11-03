package com.example.products.usecase;

import com.example.products.domain.Product;

public interface FindProductByCodeUseCase {
    Product execute(String code);
}
