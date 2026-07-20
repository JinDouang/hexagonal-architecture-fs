package com.example.catalogue.product.adapter.out.persistence;

import com.example.catalogue.product.domain.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "products")
class JpaProductEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "moodle_sync_enabled", nullable = false)
    private boolean moodleSyncEnabled;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected JpaProductEntity() {
    }

    private JpaProductEntity(UUID id, String name, String description, BigDecimal price, boolean active,
                             boolean moodleSyncEnabled, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.active = active;
        this.moodleSyncEnabled = moodleSyncEnabled;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    static JpaProductEntity fromDomain(Product product) {
        return new JpaProductEntity(
                product.id(), product.name(), product.description(), product.price(), product.active(),
                product.moodleSyncEnabled(), product.createdAt(), product.updatedAt()
        );
    }

    Product toDomain() {
        return new Product(id, name, description, price, active, moodleSyncEnabled, createdAt, updatedAt);
    }
}