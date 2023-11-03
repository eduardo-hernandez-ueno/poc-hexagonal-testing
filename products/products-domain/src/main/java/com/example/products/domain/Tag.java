package com.example.products.domain;

public record Tag(String name, String value, boolean valid) {
    public Tag(String name, String value) {
        this(name, value, true);
    }
}

