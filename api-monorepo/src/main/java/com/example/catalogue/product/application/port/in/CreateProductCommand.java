package com.example.catalogue.product.application.port.in;

import java.math.BigDecimal;

public record CreateProductCommand(String name, String description, BigDecimal price, boolean moodleSyncEnabled) {
}