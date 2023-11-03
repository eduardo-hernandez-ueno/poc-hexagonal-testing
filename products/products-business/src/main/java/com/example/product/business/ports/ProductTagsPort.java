package com.example.product.business.ports;

import com.example.products.domain.Tag;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface ProductTagsPort {

    CompletableFuture<List<Tag>> get(String code);
}
