package com.example.product.business.ports;

import com.example.products.domain.Product;

public interface ProductPort {
    Product findByCode(String code);

    Product create(Product product);

}
