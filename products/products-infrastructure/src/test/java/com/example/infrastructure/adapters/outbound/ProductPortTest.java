package com.example.infrastructure.adapters.outbound;

import com.example.infrastructure.data.repositories.holders.ProductDataHolder;
import com.example.infrastructure.data.repositories.ProductRepository;
import com.example.product.business.ports.ProductPort;
import com.example.products.domain.Product;
import com.example.products.exceptions.ProductAlreadyExistsException;
import com.mongodb.MongoWriteException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductPortTest {

    ProductRepository productRepository = Mockito.mock(ProductRepository.class);

    ProductPort port = new ProductDbAdapter(productRepository);

    @Test
    void create_whenProductNotExists_returnProduct() {
        Product newProduct = new Product("123456", "My awesome product", BigDecimal.TEN);
        ProductDataHolder newProductDataHolder = new ProductDataHolder(null, "123456", "My awesome product", BigDecimal.TEN, Map.of());
        ProductDataHolder savedProductDataHolder = new ProductDataHolder("123123131", "123456", "My awesome product", BigDecimal.TEN, Map.of());
        when(productRepository.save(newProductDataHolder)).thenReturn(savedProductDataHolder);
        Product savedProduct = port.create(newProduct);
        assertThat(savedProduct).isEqualTo(newProduct);
        verify(productRepository).save(newProductDataHolder);
    }

    @Test
    void create_whenProductExists_throwException() {
        Product newProduct = new Product("123456", "My awesome product", BigDecimal.TEN);
        when(productRepository.save(any())).thenThrow(DuplicateKeyException.class);
        assertThatThrownBy(() -> port.create(newProduct)).isInstanceOf(ProductAlreadyExistsException.class);
        verify(productRepository).save(any());
    }

    @Test
    void findByCode_whenProductExists_returnProduct() {
        String code = "123456";
        when(productRepository.findByCode(code)).thenReturn(new ProductDataHolder(null, "123456", "My awesome product", BigDecimal.TEN, Map.of()));
        assertThat(port.findByCode(code)).isEqualTo(new Product("123456", "My awesome product", BigDecimal.TEN));
        verify(productRepository).findByCode(code);
    }

    @Test
    void findByCode_whenProductNotExists_returnNull() {
        String code = "123456";
        when(productRepository.findByCode(code)).thenReturn(null);
        assertThat(port.findByCode(code)).isNull();
        verify(productRepository).findByCode(code);
    }

}
