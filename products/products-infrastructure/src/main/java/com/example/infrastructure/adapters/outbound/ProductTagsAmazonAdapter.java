package com.example.infrastructure.adapters.outbound;

import com.example.infrastructure.data.clients.AmazonClient;
import com.example.product.business.ports.ProductTagsPort;
import com.example.products.domain.Tag;
import com.example.products.exceptions.TagsNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class ProductTagsAmazonAdapter implements ProductTagsPort {

    private final AmazonClient amazonClient;

    public ProductTagsAmazonAdapter(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @Override
    public CompletableFuture<List<Tag>> get(String code) {
        return CompletableFuture.supplyAsync(() -> amazonClient.getTags(code))
                .thenApply((tagList) -> tagList.stream().map(tag -> new Tag(tag.key(), tag.value())).toList())
                .exceptionally((throwable) -> {
                    throw new TagsNotFoundException(throwable);
                });
    }
}
