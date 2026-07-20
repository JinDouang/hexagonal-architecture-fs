package com.example.catalogue.product.adapter.out.moodle;

import com.example.catalogue.product.application.port.out.MoodleProductSyncPort;
import com.example.catalogue.product.domain.Product;

final class NoOpMoodleProductSyncAdapter implements MoodleProductSyncPort {

    @Override
    public void productActivated(Product product) {
    }

    @Override
    public void productDeactivated(Product product) {
    }
}