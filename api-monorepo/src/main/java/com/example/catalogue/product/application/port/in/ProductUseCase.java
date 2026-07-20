package com.example.catalogue.product.application.port.in;

import com.example.catalogue.product.domain.Product;
import java.util.List;
import java.util.UUID;

public interface ProductUseCase {

    List<Product> listProducts();

    Product getProduct(UUID id);

    Product createProduct(CreateProductCommand command);

    Product updateProduct(UUID id, UpdateProductCommand command);

    void deleteProduct(UUID id);

    Product activateProduct(UUID id);

    Product deactivateProduct(UUID id);
}