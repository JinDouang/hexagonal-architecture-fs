package com.example.catalogue.product.application;

import java.util.UUID;

public class ProductNotFoundException extends RuntimeException {

    private final UUID productId;

    public ProductNotFoundException(UUID productId) {
        super("Product not found: " + productId);
        this.productId = productId;
    }

    public UUID productId() {
        return productId;
    }
}