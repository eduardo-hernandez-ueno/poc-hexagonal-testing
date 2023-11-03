package com.example.infrastructure.adapters.outbound;

import com.example.infrastructure.data.repositories.holders.ProductDataHolder;
import com.example.infrastructure.data.repositories.ProductRepository;
import com.example.product.business.ports.ProductPort;
import com.example.products.domain.Product;
import com.example.products.domain.Tag;
import com.example.products.exceptions.ProductAlreadyExistsException;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProductDbAdapter implements ProductPort {

    private final ProductRepository productRepository;

    public ProductDbAdapter(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product findByCode(String code) {
        ProductDataHolder product = productRepository.findByCode(code);
        return Objects.isNull(product) ? null : map(product);
    }

    @Override
    public Product create(Product product) {
        try {
            return map(productRepository.save(map(product)));
        } catch (DuplicateKeyException e) {
            throw new ProductAlreadyExistsException(e);
        }
    }

    private Product map(ProductDataHolder productDataHolder) {
        return new Product(productDataHolder.code(), productDataHolder.name(), productDataHolder.price(),
                productDataHolder.tags().keySet().stream()
                        .map(tagDbName -> new Tag(tagDbName, productDataHolder.tags().get(tagDbName))).collect(Collectors.toList()));
    }

    private ProductDataHolder map(Product product) {
        return new ProductDataHolder(null, product.code(), product.name(), product.price(), product.tags().stream().collect(Collectors.toMap(Tag::name, Tag::value)));
    }
}
