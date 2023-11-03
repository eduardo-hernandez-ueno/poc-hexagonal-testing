package com.example.products.exceptions;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
