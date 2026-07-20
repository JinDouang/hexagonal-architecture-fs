package com.example.catalogue.product.adapter.out.moodle;

import com.example.catalogue.product.domain.Product;
import java.math.BigDecimal;

record MoodleProductPayload(String id, String name, String description, BigDecimal price, boolean active) {

    static MoodleProductPayload from(Product product) {
        return new MoodleProductPayload(
                product.id().toString(), product.name(), product.description(), product.price(), product.active()
        );
    }
}