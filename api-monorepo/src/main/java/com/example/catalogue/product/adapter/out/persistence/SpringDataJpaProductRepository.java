package com.example.catalogue.product.adapter.out.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

interface SpringDataJpaProductRepository extends JpaRepository<JpaProductEntity, UUID> {
}