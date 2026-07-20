package com.example.catalogue.product.adapter.in.web;

import com.example.catalogue.product.application.port.in.CreateProductCommand;
import com.example.catalogue.product.application.port.in.UpdateProductCommand;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank @Size(max = 120) String name,
        @Size(max = 500) String description,
        @NotNull @DecimalMin("0.0") BigDecimal price,
        boolean moodleSyncEnabled
) {

    CreateProductCommand toCreateCommand() {
        return new CreateProductCommand(name, description, price, moodleSyncEnabled);
    }

    UpdateProductCommand toUpdateCommand() {
        return new UpdateProductCommand(name, description, price, moodleSyncEnabled);
    }
}