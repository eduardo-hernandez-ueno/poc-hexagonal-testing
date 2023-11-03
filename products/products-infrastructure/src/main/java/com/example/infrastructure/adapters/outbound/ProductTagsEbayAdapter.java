package com.example.infrastructure.adapters.outbound;

import com.example.infrastructure.data.clients.EbayClient;
import com.example.infrastructure.data.clients.dtos.ebay.EbayTagRequestDto;
import com.example.product.business.ports.ProductTagsPort;
import com.example.products.domain.Tag;
import com.example.products.exceptions.TagsNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class ProductTagsEbayAdapter implements ProductTagsPort {

    private final EbayClient ebayClient;

    public ProductTagsEbayAdapter(EbayClient ebayClient) {
        this.ebayClient = ebayClient;
    }

    @Override
    public CompletableFuture<List<Tag>> get(String code) {
        return CompletableFuture.supplyAsync(() -> ebayClient.getTags(new EbayTagRequestDto(code)))
                .thenApply((tagList) -> tagList.stream().map(tag -> new Tag(tag.key(), tag.value(), !tag.inRevision())).toList())
                .exceptionally((throwable) -> {
                    throw new TagsNotFoundException(throwable);
                });
    }
}
