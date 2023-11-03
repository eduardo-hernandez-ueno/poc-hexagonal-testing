package com.example.product.business.usecase;

import com.example.product.business.ports.ProductPort;
import com.example.product.business.ports.ProductTagsPort;
import com.example.products.domain.Product;
import com.example.products.domain.Tag;
import com.example.products.usecase.CreateProductUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class CreateProductUseCaseImpl implements CreateProductUseCase {
    private final Logger logger = LoggerFactory.getLogger(CreateProductUseCaseImpl.class);
    private final ProductPort productPort;
    private final List<ProductTagsPort> productTagsPorts;

    public CreateProductUseCaseImpl(ProductPort productPort, List<ProductTagsPort> productTagsPorts) {
        this.productPort = productPort;
        this.productTagsPorts = productTagsPorts;
    }

    @Override
    public Product execute(Product product) {
        List<CompletableFuture<List<Tag>>> completableFutures = productTagsPorts.stream()
                .map(port -> port.get(product.code())).toList();
        List<Tag> tags = completableFutures.stream()
                .flatMap(f -> f.exceptionally((ex) -> List.of()).join().stream())
                .filter(Tag::valid)
                .filter(distinctByKey(Tag::name))
                .toList();
        return productPort.create(product.tagged(tags));
    }

    public static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
