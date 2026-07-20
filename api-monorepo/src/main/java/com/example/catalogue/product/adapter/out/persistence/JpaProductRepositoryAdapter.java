package com.example.catalogue.product.adapter.out.persistence;

import com.example.catalogue.product.application.port.out.ProductRepositoryPort;
import com.example.catalogue.product.domain.Product;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
class JpaProductRepositoryAdapter implements ProductRepositoryPort {

    private final SpringDataJpaProductRepository repository;

    JpaProductRepositoryAdapter(SpringDataJpaProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
                .map(JpaProductEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Product> findById(UUID id) {
        return repository.findById(id).map(JpaProductEntity::toDomain);
    }

    @Override
    public Product save(Product product) {
        return repository.save(JpaProductEntity.fromDomain(product)).toDomain();
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}