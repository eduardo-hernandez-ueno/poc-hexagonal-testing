package com.example.products.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.List;

import java.util.Optional;
import java.util.stream.Stream;


import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ProductTest {

    @ParameterizedTest
    @MethodSource("validProducts")
    void construction_withValidParams_returnProductWithAllFieldInitialized(String code, String name, BigDecimal price, List<Tag> tags) {
        Product p = new Product(code, name, price, tags);

        assertThat(p.code()).isEqualTo(code);
        assertThat(p.name()).isEqualTo(name);
        assertThat(p.price()).isEqualTo(Optional.ofNullable(price).orElse(BigDecimal.ZERO));
        assertThat(p.tags()).isEqualTo(Optional.ofNullable(tags).orElse(List.of()));
    }

    @ParameterizedTest
    @MethodSource("validProducts")
    void construction_withValidParamsOverConstructorWithoutTags_returnProductWithAllFieldInitialized(String code, String name, BigDecimal price) {
        Product p = new Product(code, name, price);

        assertThat(p.code()).isEqualTo(code);
        assertThat(p.name()).isEqualTo(name);
        assertThat(p.price()).isEqualTo(Optional.ofNullable(price).orElse(BigDecimal.ZERO));
        assertThat(p.tags()).isEmpty();
    }

    @ParameterizedTest
    @MethodSource("invalidProducts")
    void construction_withInvalidParams_throwException(String code, String name, BigDecimal price, String expectedMessage) {
        assertThatThrownBy(() -> new Product(code, name, price))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(expectedMessage);
    }

    @MethodSource
    static Stream<Arguments> validProducts() {
        return Stream.of(
                Arguments.of("123456", "My product", null, null),
                Arguments.of("A23456", "My product", null, null),
                Arguments.of("123456", "My product with max name posible", null, null),
                Arguments.of("123456", "My product", new BigDecimal("100"), null),
                Arguments.of("123456", "My product", new BigDecimal("100"), List.of(new Tag("tag 1", "value 1"), new Tag("tag 2", "value 2")))
        );
    }

    @MethodSource
    static Stream<Arguments> invalidProducts() {
        return Stream.of(
                Arguments.of("123456A", "My product", null, "Code is too long (Max 6)"),
                Arguments.of("A23456", "My product with extra long name no fake", null, "Name is too long (Max 32)"),
                Arguments.of("A2345", "My product", null, "Code is too short (Min 6)"),
                Arguments.of(null, "My product with max name posible", null, "Code cannot be null or empty"),
                Arguments.of("", "My product with max name posible", null, "Code cannot be null or empty"),
                Arguments.of("A23456", null, null, "Name cannot be null or empty"),
                Arguments.of("A23456", "", null, "Name cannot be null or empty"),
                Arguments.of("123456", "My product", new BigDecimal("-100"), "Price must be greater than or equals to 0")
        );
    }

    @Test
    void changePrice_withValidPrice_returnProductWithPriceChanged() {
        Product p = new Product("123456", "My product", new BigDecimal(5));
        Product modifiedProduct = p.changePrice(new BigDecimal(10));
        assertThat(p.price()).isEqualTo(new BigDecimal(5));
        assertThat(modifiedProduct.price()).isEqualTo(new BigDecimal(10));
    }

    @Test
    void changePrice_withInvalidPrice_throwException() {
        Product p = new Product("123456", "My product", null);
        assertThatThrownBy(() -> {
            p.changePrice(new BigDecimal(-10));
        }).isInstanceOf(IllegalArgumentException.class);
    }

}
