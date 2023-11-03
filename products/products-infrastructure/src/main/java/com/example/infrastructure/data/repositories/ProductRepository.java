package com.example.infrastructure.data.repositories;

import com.example.infrastructure.data.repositories.holders.ProductDataHolder;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<ProductDataHolder, Long> {
    ProductDataHolder findByCode(String code);
}
