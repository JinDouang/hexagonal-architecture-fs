package com.example.catalogue.product.adapter.out.moodle;

import com.example.catalogue.product.application.port.out.MoodleProductSyncPort;
import com.example.catalogue.product.domain.Product;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@ConditionalOnProperty(prefix = "app.moodle", name = "enabled", havingValue = "true")
class MoodleProductSyncAdapter implements MoodleProductSyncPort {

    private final RestClient restClient;

    MoodleProductSyncAdapter(RestClient.Builder restClientBuilder, MoodleProperties properties) {
        this.restClient = restClientBuilder.baseUrl(properties.baseUrl()).build();
    }

    @Override
    public void productActivated(Product product) {
        post("/api/products/{id}/activated", product);
    }

    @Override
    public void productDeactivated(Product product) {
        post("/api/products/{id}/deactivated", product);
    }

    private void post(String path, Product product) {
        restClient.post()
                .uri(path, product.id())
                .body(MoodleProductPayload.from(product))
                .retrieve()
                .toBodilessEntity();
    }
}