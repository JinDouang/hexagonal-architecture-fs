package com.example.catalogue.product.application.port.out;

import com.example.catalogue.product.domain.Product;

public interface MoodleProductSyncPort {

    void productActivated(Product product);

    void productDeactivated(Product product);
}