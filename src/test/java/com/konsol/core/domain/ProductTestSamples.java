package com.konsol.core.domain;

import java.util.UUID;

public class ProductTestSamples {

    public static Product getProductSample1() {
        return new Product().id("id1").productId("productId1");
    }

    public static Product getProductSample2() {
        return new Product().id("id2").productId("productId2");
    }

    public static Product getProductRandomSampleGenerator() {
        return new Product().id(UUID.randomUUID().toString()).productId(UUID.randomUUID().toString());
    }
}
