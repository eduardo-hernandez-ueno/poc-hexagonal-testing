package com.example.infrastructure.controller.dtos;

import com.example.products.domain.Tag;

import java.math.BigDecimal;
import java.util.Map;

public record ProductDto(String code, String name, BigDecimal price, Map<String, String> tags) {
}
