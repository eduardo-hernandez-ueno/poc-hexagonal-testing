package com.example.product.business;

import com.example.product.business.ports.ProductPort;
import com.example.product.business.usecase.FindProductByCodeUseCaseImpl;
import com.example.products.domain.Product;
import com.example.products.exceptions.ProductNotExistsException;
import com.example.products.usecase.FindProductByCodeUseCase;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FindProductByCodeUseCaseTest {

    private final ProductPort productPort = mock(ProductPort.class);
    private final FindProductByCodeUseCase findProductByIdUseCase = new FindProductByCodeUseCaseImpl(productPort);

    @Test
    void execute_whenExistsProduct_returnProduct() {
        when(productPort.findByCode("123456")).thenReturn(new Product("123456", "My Product", BigDecimal.TEN));

        Product product = findProductByIdUseCase.execute("123456");
        assertThat(product).extracting(Product::code, Product::name, Product::price)
                .contains("123456", "My Product", BigDecimal.TEN);
    }

    @Test
    void execute_whenNotExistsProduct_throwException() {
        when(productPort.findByCode("123456")).thenReturn(null);

        assertThatThrownBy(() -> findProductByIdUseCase.execute("123456"))
                .isInstanceOf(ProductNotExistsException.class);
    }
}
