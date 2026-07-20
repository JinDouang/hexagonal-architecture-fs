package com.example.catalogue.product.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.catalogue.product.application.port.in.CreateProductCommand;
import com.example.catalogue.product.application.port.out.MoodleProductSyncPort;
import com.example.catalogue.product.application.port.out.ProductRepositoryPort;
import com.example.catalogue.product.domain.Product;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ProductServiceTest {

    private final Clock fixedClock = Clock.fixed(Instant.parse("2026-07-20T12:00:00Z"), ZoneOffset.UTC);

    @Test
    void createsInactiveProductByDefault() {
        InMemoryProductRepository repository = new InMemoryProductRepository();
        SpyMoodleProductSync moodleSync = new SpyMoodleProductSync();
        ProductService service = new ProductService(repository, moodleSync, fixedClock);

        Product created = service.createProduct(new CreateProductCommand("  Angular  ", " SPA ", BigDecimal.TEN, true));

        assertEquals("Angular", created.name());
        assertEquals("SPA", created.description());
        assertFalse(created.active());
        assertTrue(created.moodleSyncEnabled());
        assertTrue(moodleSync.activatedIds.isEmpty());
    }

    @Test
    void activatesProductAndSynchronizesWithMoodleWhenEnabled() {
        InMemoryProductRepository repository = new InMemoryProductRepository();
        SpyMoodleProductSync moodleSync = new SpyMoodleProductSync();
        ProductService service = new ProductService(repository, moodleSync, fixedClock);
        Product created = service.createProduct(new CreateProductCommand("Spring", "Boot", BigDecimal.valueOf(49), true));

        Product activated = service.activateProduct(created.id());

        assertTrue(activated.active());
        assertEquals(List.of(created.id()), moodleSync.activatedIds);
    }

    @Test
    void rejectsUnknownProductActivation() {
        ProductService service = new ProductService(new InMemoryProductRepository(), new SpyMoodleProductSync(), fixedClock);

        assertThrows(ProductNotFoundException.class, () -> service.activateProduct(UUID.randomUUID()));
    }

    private static final class InMemoryProductRepository implements ProductRepositoryPort {

        private final Map<UUID, Product> products = new HashMap<>();

        @Override
        public List<Product> findAll() {
            return List.copyOf(products.values());
        }

        @Override
        public Optional<Product> findById(UUID id) {
            return Optional.ofNullable(products.get(id));
        }

        @Override
        public Product save(Product product) {
            products.put(product.id(), product);
            return product;
        }

        @Override
        public boolean existsById(UUID id) {
            return products.containsKey(id);
        }

        @Override
        public void deleteById(UUID id) {
            products.remove(id);
        }
    }

    private static final class SpyMoodleProductSync implements MoodleProductSyncPort {

        private final List<UUID> activatedIds = new ArrayList<>();
        private final List<UUID> deactivatedIds = new ArrayList<>();

        @Override
        public void productActivated(Product product) {
            activatedIds.add(product.id());
        }

        @Override
        public void productDeactivated(Product product) {
            deactivatedIds.add(product.id());
        }
    }
}