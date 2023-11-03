package com.example.infrastructure.data.repositories.holders;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Map;

@Document("products")
public record ProductDataHolder(@Id String id, @Indexed(unique = true) String code, String name, BigDecimal price,
                                Map<String, String> tags) {
}
