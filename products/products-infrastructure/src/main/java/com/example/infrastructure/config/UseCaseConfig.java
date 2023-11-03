package com.example.infrastructure.config;

import com.example.product.business.ports.ProductPort;
import com.example.product.business.ports.ProductTagsPort;
import com.example.product.business.usecase.CreateProductUseCaseImpl;
import com.example.product.business.usecase.FindProductByCodeUseCaseImpl;
import com.example.products.usecase.CreateProductUseCase;
import com.example.products.usecase.FindProductByCodeUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UseCaseConfig {

    @Bean
    public FindProductByCodeUseCase findProductByCodeUseCase(ProductPort port) {
        return new FindProductByCodeUseCaseImpl(port);
    }

    @Bean
    public CreateProductUseCase createProductUseCase(ProductPort productPort, List<ProductTagsPort> productTagsPorts) {
        return new CreateProductUseCaseImpl(productPort, productTagsPorts);
    }
}
