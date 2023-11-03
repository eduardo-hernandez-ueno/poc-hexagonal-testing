package com.example.infrastructure.adapters.inbound;

import com.example.infrastructure.controller.dtos.ProductDto;
import com.example.infrastructure.controller.dtos.ProductRequestDto;
import com.example.products.domain.Product;
import com.example.products.usecase.CreateProductUseCase;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreateProductHandlerTest {

    private CreateProductUseCase useCase = mock(CreateProductUseCase.class);
    private Function<Product, ProductDto> mapToResponse = mock(Function.class);
    private Function<ProductRequestDto, Product> mapToDomain = mock(Function.class);

    private CreateProductHandler<ProductRequestDto, ProductDto> handler = new CreateProductHandler<>(useCase, mapToDomain, mapToResponse);

    @Test
    void apply_whenUseCaseOk_callUseCaseAndBothMappers() {
        handler.apply(mock(ProductRequestDto.class));

        verify(useCase).execute(any());
        verify(mapToResponse).apply(any());
        verify(mapToDomain).apply(any());
    }

    @Test
    void apply_whenUseCaseFail_RaiseExceptionAndCallUseCaseAndRequestMapper() {

        when(useCase.execute(any())).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> handler.apply(mock(ProductRequestDto.class)))
                .isInstanceOf(RuntimeException.class);

        verify(useCase).execute(any());
        verify(mapToResponse, never()).apply(any());
        verify(mapToDomain).apply(any());
    }
}
