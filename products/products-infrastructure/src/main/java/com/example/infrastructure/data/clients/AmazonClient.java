package com.example.infrastructure.data.clients;

import com.example.infrastructure.data.clients.dtos.amazon.AmazonTagResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@FeignClient(name = "amznClient", url = "${test.clients.amazon.url}")
public interface AmazonClient {

    @GetMapping("/tags/{code}")
    List<AmazonTagResponseDto> getTags(@PathVariable String code);
}
