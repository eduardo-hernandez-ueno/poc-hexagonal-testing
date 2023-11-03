package com.example.infrastructure.controller.dtos;

import java.math.BigDecimal;

public record ProductRequestDto(String code, String name, BigDecimal price) {
}
