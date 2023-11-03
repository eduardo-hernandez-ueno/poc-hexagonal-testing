package com.example.infrastructure.data.clients;

import com.example.infrastructure.data.clients.dtos.ebay.EbayTagRequestDto;
import com.example.infrastructure.data.clients.dtos.ebay.EbayTagResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@FeignClient(name = "ebayClient", url = "${test.clients.ebay.url}")
public interface EbayClient {

    @PostMapping("/product/tags")
    List<EbayTagResponseDto> getTags(@RequestBody EbayTagRequestDto ebayTagRequestDto);
}
