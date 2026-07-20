package com.example.catalogue.product.application.port.out;

import com.example.catalogue.product.domain.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepositoryPort {

    List<Product> findAll();

    Optional<Product> findById(UUID id);

    Product save(Product product);

    boolean existsById(UUID id);

    void deleteById(UUID id);
}