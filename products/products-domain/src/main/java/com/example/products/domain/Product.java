package com.example.products.domain;


import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public record Product(String code, String name, BigDecimal price, List<Tag> tags) {
    public Product(String code, String name, BigDecimal price) {
        this(code, name, price, null);
    }

    public Product(String code, String name, BigDecimal price, List<Tag> tags) {
        this.code = validateCode(code);
        this.name = validateName(name);
        this.price = validatePrice(price);
        this.tags = Optional.ofNullable(tags).orElse(List.of());
    }

    private static BigDecimal validatePrice(BigDecimal price) {
        if (price == null) {
            return BigDecimal.ZERO;
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price must be greater than or equals to 0");
        }
        return price;
    }

    private static String validateName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (name.length() > 32) {
            throw new IllegalArgumentException("Name is too long (Max 32)");
        }
        return name;
    }

    public static String validateCode(String code) {
        if (StringUtils.isBlank(code)) {
            throw new IllegalArgumentException("Code cannot be null or empty");
        }
        if (code.length() > 6) {
            throw new IllegalArgumentException("Code is too long (Max 6)");
        }
        if (code.length() < 6) {
            throw new IllegalArgumentException("Code is too short (Min 6)");
        }
        return code;
    }

    public Product changePrice(BigDecimal newPrice) {
        return new Product(code, name, newPrice);
    }

    public Product tagged(List<Tag> tags) {
        return new Product(code, name, price, tags);
    }

}
