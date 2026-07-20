package com.example.catalogue.product.application;

import com.example.catalogue.product.application.port.in.CreateProductCommand;
import com.example.catalogue.product.application.port.in.ProductUseCase;
import com.example.catalogue.product.application.port.in.UpdateProductCommand;
import com.example.catalogue.product.application.port.out.MoodleProductSyncPort;
import com.example.catalogue.product.application.port.out.ProductRepositoryPort;
import com.example.catalogue.product.domain.Product;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class ProductService implements ProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final MoodleProductSyncPort moodleProductSyncPort;
    private final Clock clock;

    public ProductService(ProductRepositoryPort productRepositoryPort, MoodleProductSyncPort moodleProductSyncPort, Clock clock) {
        this.productRepositoryPort = productRepositoryPort;
        this.moodleProductSyncPort = moodleProductSyncPort;
        this.clock = clock;
    }

    @Override
    public List<Product> listProducts() {
        return productRepositoryPort.findAll();
    }

    @Override
    public Product getProduct(UUID id) {
        return productRepositoryPort.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public Product createProduct(CreateProductCommand command) {
        Product product = Product.create(
                UUID.randomUUID(),
                command.name(),
                command.description(),
                command.price(),
                command.moodleSyncEnabled(),
                now()
        );
        return productRepositoryPort.save(product);
    }

    @Override
    public Product updateProduct(UUID id, UpdateProductCommand command) {
        Product product = getProduct(id).updateDetails(
                command.name(),
                command.description(),
                command.price(),
                command.moodleSyncEnabled(),
                now()
        );
        return productRepositoryPort.save(product);
    }

    @Override
    public void deleteProduct(UUID id) {
        if (!productRepositoryPort.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        productRepositoryPort.deleteById(id);
    }

    @Override
    public Product activateProduct(UUID id) {
        Product current = getProduct(id);
        Product activated = productRepositoryPort.save(current.activate(now()));
        if (!current.active() && activated.moodleSyncEnabled()) {
            moodleProductSyncPort.productActivated(activated);
        }
        return activated;
    }

    @Override
    public Product deactivateProduct(UUID id) {
        Product current = getProduct(id);
        Product deactivated = productRepositoryPort.save(current.deactivate(now()));
        if (current.active() && deactivated.moodleSyncEnabled()) {
            moodleProductSyncPort.productDeactivated(deactivated);
        }
        return deactivated;
    }

    private Instant now() {
        return clock.instant();
    }
}