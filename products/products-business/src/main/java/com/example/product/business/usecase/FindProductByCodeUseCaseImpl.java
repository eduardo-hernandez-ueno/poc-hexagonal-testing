package com.example.product.business.usecase;

import com.example.product.business.ports.ProductPort;
import com.example.products.domain.Product;
import com.example.products.exceptions.ProductNotExistsException;
import com.example.products.usecase.FindProductByCodeUseCase;

public class FindProductByCodeUseCaseImpl implements FindProductByCodeUseCase {

    private final ProductPort productPort;

    public FindProductByCodeUseCaseImpl(ProductPort productPort) {
        this.productPort = productPort;
    }

    @Override
    public Product execute(String code) {
        Product product = productPort.findByCode(code);
        if (product == null) throw new ProductNotExistsException();
        return product;
    }
}
