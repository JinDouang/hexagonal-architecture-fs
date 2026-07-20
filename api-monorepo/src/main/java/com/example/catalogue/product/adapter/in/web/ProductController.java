package com.example.catalogue.product.adapter.in.web;

import com.example.catalogue.product.application.port.in.ProductUseCase;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/products")
class ProductController {

    private final ProductUseCase productUseCase;

    ProductController(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }

    @GetMapping
    List<ProductResponse> listProducts() {
        return productUseCase.listProducts().stream()
                .map(ProductResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    ProductResponse getProduct(@PathVariable UUID id) {
        return ProductResponse.from(productUseCase.getProduct(id));
    }

    @PostMapping
    ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = ProductResponse.from(productUseCase.createProduct(request.toCreateCommand()));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{id}")
    ProductResponse updateProduct(@PathVariable UUID id, @Valid @RequestBody ProductRequest request) {
        return ProductResponse.from(productUseCase.updateProduct(id, request.toUpdateCommand()));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productUseCase.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    ProductResponse activateProduct(@PathVariable UUID id) {
        return ProductResponse.from(productUseCase.activateProduct(id));
    }

    @PatchMapping("/{id}/deactivate")
    ProductResponse deactivateProduct(@PathVariable UUID id) {
        return ProductResponse.from(productUseCase.deactivateProduct(id));
    }
}