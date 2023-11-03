package com.example.infrastructure.adapters.inbound;

import com.example.products.domain.Product;
import com.example.products.usecase.FindProductByCodeUseCase;

import java.util.function.Function;

public class FindProductByCodeHandler<T> implements Function<String, T> {

    private final FindProductByCodeUseCase useCase;
    private final Function<Product, T> mapper;

    public FindProductByCodeHandler(FindProductByCodeUseCase useCase, Function<Product, T> mapper) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    @Override
    public T apply(String code) {
        return map(useCase.execute(code));
    }

    private T map(Product product) {
        return mapper.apply(product);
    }
}
