package com.example.infrastructure.adapters.outbound;

import com.example.infrastructure.data.clients.AmazonClient;
import com.example.infrastructure.data.clients.dtos.amazon.AmazonTagResponseDto;
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

public class ProductTagsPortAmazonAdapterTest {

    AmazonClient amazonClient = Mockito.mock(AmazonClient.class);

    ProductTagsPort port = new ProductTagsAmazonAdapter(amazonClient);

    @Test
    void get_whenTagsExistsInAmazon_returnListOfTags() {
        when(amazonClient.getTags("123456")).thenReturn(List.of(new AmazonTagResponseDto("key1", "value1"),
                new AmazonTagResponseDto("key2", "value2")));

        assertThat(port.get("123456")).succeedsWithin(Duration.of(10, ChronoUnit.SECONDS))
                .isEqualTo(List.of(
                new Tag("key1", "value1"),
                new Tag("key2", "value2")
        ));
    }

    @Test
    void get_whenAmazonRespondError_throwError() throws Exception {
        when(amazonClient.getTags("123456"))
                .thenThrow(mock(FeignException.FeignClientException.class));

        assertThat(port.get("123456")).failsWithin(Duration.of(10, ChronoUnit.SECONDS))
                .withThrowableOfType(ExecutionException.class)
                .withCauseInstanceOf(TagsNotFoundException.class);
    }
}
