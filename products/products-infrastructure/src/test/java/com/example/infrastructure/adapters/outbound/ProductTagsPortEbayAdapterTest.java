package com.example.infrastructure.adapters.outbound;

import com.example.infrastructure.data.clients.EbayClient;
import com.example.infrastructure.data.clients.dtos.ebay.EbayTagRequestDto;
import com.example.infrastructure.data.clients.dtos.ebay.EbayTagResponseDto;
import com.example.product.business.ports.ProductTagsPort;
import com.example.products.domain.Tag;
import com.example.products.exceptions.TagsNotFoundException;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProductTagsPortEbayAdapterTest {
    EbayClient ebayClient = Mockito.mock(EbayClient.class);

    ProductTagsPort port = new ProductTagsEbayAdapter(ebayClient);


    @Test
    void get_whenTagsExistsInAmazon_returnListOfTags() {
        when(ebayClient.getTags(new EbayTagRequestDto("123456")))
                .thenReturn(List.of(new EbayTagResponseDto("key1", "value1", true),
                        new EbayTagResponseDto("key2", "value2", false)));

        assertThat(port.get("123456")).succeedsWithin(Duration.of(10, ChronoUnit.SECONDS))
                .isEqualTo(List.of(
                new Tag("key1", "value1", false),
                new Tag("key2", "value2", true)
        ));
    }

    @Test
    void get_whenAmazonRespondError_throwError() throws Exception {
        when(ebayClient.getTags(new EbayTagRequestDto("123456")))
                .thenThrow(mock(FeignException.FeignClientException.class));

        assertThat(port.get("123456")).failsWithin(Duration.of(10, ChronoUnit.SECONDS))
                .withThrowableOfType(ExecutionException.class)
                .withCauseInstanceOf(TagsNotFoundException.class);
    }
}
