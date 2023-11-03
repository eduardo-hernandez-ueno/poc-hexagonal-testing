package com.example.infrastructure.adapters.inbound;

import com.example.infrastructure.controller.dtos.ProductDto;
import com.example.products.domain.Product;
import com.example.products.usecase.FindProductByCodeUseCase;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FindProductByCodeHandlerTest {

    FindProductByCodeUseCase useCase = mock(FindProductByCodeUseCase.class);
    Function<Product, ProductDto> mapper = mock(Function.class);

    FindProductByCodeHandler<ProductDto> findProductByCodeHandler = new FindProductByCodeHandler<>(useCase, mapper);


    @Test
    void apply_whenUseCaseOk_mustCallUseCaseAndMapper() {
        findProductByCodeHandler.apply("anyCode");

        verify(useCase).execute("anyCode");
        verify(mapper).apply(any());
    }

    @Test
    void apply_whenUseCaseFail_raiseExceptionAndNotCallMapper() {
        when(useCase.execute(any())).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> findProductByCodeHandler.apply("anyCode")).isInstanceOf(RuntimeException.class);
        verify(useCase).execute("anyCode");
        verify(mapper, never()).apply(any());
    }

}
