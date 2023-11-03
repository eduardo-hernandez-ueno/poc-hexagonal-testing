package com.example.product.business;

import com.example.product.business.ports.ProductPort;
import com.example.product.business.ports.ProductTagsPort;
import com.example.product.business.usecase.CreateProductUseCaseImpl;
import com.example.products.domain.Product;
import com.example.products.domain.Tag;
import com.example.products.exceptions.TagsNotFoundException;
import com.example.products.usecase.CreateProductUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class CreateProductUseCaseTest {

    ProductPort productPort = Mockito.mock(ProductPort.class);
    ProductTagsPort amazonPort = mock(ProductTagsPort.class);
    ProductTagsPort ebayPort = mock(ProductTagsPort.class);
    CreateProductUseCase useCase = new CreateProductUseCaseImpl(productPort, List.of(amazonPort, ebayPort));

    @Test
    void execute_withValidProduct_returnProduct() {
        Product p = new Product("123456", "My awesome Product", BigDecimal.TEN);
        when(productPort.create(p)).thenReturn(p);

        Product product = useCase.execute(p);

        assertThat(product).isEqualTo(p);
        verify(productPort).create(p);
    }

    @Test
    void execute_whenExistsTags_returnProductWithTags() {
        Product p = new Product("123456", "My awesome Product", BigDecimal.TEN);
        when(amazonPort.get("123456")).thenReturn(CompletableFuture.completedFuture(List.of(new Tag("tag 1", "value 1"))));
        when(ebayPort.get("123456")).thenReturn(CompletableFuture.completedFuture(List.of(new Tag("tag 1", "value n", true), new Tag("tag 2", "value 2", true), new Tag("tag 3", "value 3", false))));
        when(productPort.create(any(Product.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        Product product = useCase.execute(p);

        assertThat(product.tags()).hasSize(2);

        verify(productPort).create(any(Product.class));
        verify(amazonPort).get("123456");
        verify(ebayPort).get("123456");
    }

    @Test
    void execute_whenATagPortFails_returnProductWithTags() {
        Product p = new Product("123456", "My awesome Product", BigDecimal.TEN);
        when(amazonPort.get("123456")).thenReturn(CompletableFuture.failedFuture(new TagsNotFoundException(new RuntimeException())));
        when(ebayPort.get("123456")).thenReturn(CompletableFuture.completedFuture(List.of(new Tag("tag 1", "value n", true), new Tag("tag 2", "value 2", true), new Tag("tag 3", "value 3", false))));
        when(productPort.create(any(Product.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        Product product = useCase.execute(p);

        assertThat(product.tags()).hasSize(2);

        verify(productPort).create(any(Product.class));
        verify(amazonPort).get("123456");
        verify(ebayPort).get("123456");
    }


    @Test
    void execute_whenAllTagPortFails_returnProductWithTags() {
        Product p = new Product("123456", "My awesome Product", BigDecimal.TEN);
        when(amazonPort.get("123456")).thenReturn(CompletableFuture.failedFuture(new TagsNotFoundException(new RuntimeException())));
        when(ebayPort.get("123456")).thenReturn(CompletableFuture.failedFuture(new TagsNotFoundException(new RuntimeException())));
        when(productPort.create(any(Product.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);

        Product product = useCase.execute(p);

        assertThat(product.tags()).isEmpty();

        verify(productPort).create(any(Product.class));
        verify(amazonPort).get("123456");
        verify(ebayPort).get("123456");
    }

    @Test
    void execute_whenPortThrowException_promoteException() {
        Product p = new Product("123456", "My awesome Product", BigDecimal.TEN);
        when(productPort.create(p)).thenThrow(RuntimeException.class);

        assertThatThrownBy(() -> useCase.execute(p))
                .isInstanceOf(RuntimeException.class);

        verify(productPort).create(p);
    }
}
