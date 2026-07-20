package com.example.catalogue.product;

import com.example.catalogue.product.application.ProductService;
import com.example.catalogue.product.application.port.in.ProductUseCase;
import com.example.catalogue.product.application.port.out.MoodleProductSyncPort;
import com.example.catalogue.product.application.port.out.ProductRepositoryPort;
import com.example.catalogue.product.domain.Product;
import java.time.Clock;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ProductModuleConfiguration {

    @Bean
    ProductUseCase productUseCase(ProductRepositoryPort productRepositoryPort, MoodleProductSyncPort moodleProductSyncPort) {
        return new ProductService(productRepositoryPort, moodleProductSyncPort, Clock.systemUTC());
    }

    @Bean
    @ConditionalOnMissingBean(MoodleProductSyncPort.class)
    MoodleProductSyncPort noOpMoodleProductSyncPort() {
        return new MoodleProductSyncPort() {
            @Override
            public void productActivated(Product product) {
            }

            @Override
            public void productDeactivated(Product product) {
            }
        };
    }
}