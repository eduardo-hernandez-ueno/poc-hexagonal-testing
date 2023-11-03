package com.example.infrastructure.adapters.inbound;

import com.example.products.domain.Product;
import com.example.products.usecase.CreateProductUseCase;

import java.util.function.Function;

public class CreateProductHandler<T, R> implements Function<T, R> {

    private CreateProductUseCase useCase;
    private Function<T, Product> mapToDomain;
    private Function<Product, R> mapToResponse;

    public CreateProductHandler(CreateProductUseCase useCase, Function<T, Product> mapToDomain, Function<Product, R> mapToResponse) {
        this.useCase = useCase;
        this.mapToDomain = mapToDomain;
        this.mapToResponse = mapToResponse;
    }

    @Override
    public R apply(T t) {
        return mapToResponse.apply(useCase.execute(mapToDomain.apply(t)));
    }

}
