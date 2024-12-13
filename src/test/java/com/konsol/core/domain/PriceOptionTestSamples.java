package com.konsol.core.domain;

import java.util.UUID;

public class PriceOptionTestSamples {

    public static PriceOption getPriceOptionSample1() {
        return new PriceOption().id("id1").name("name1").productId("productId1");
    }

    public static PriceOption getPriceOptionSample2() {
        return new PriceOption().id("id2").name("name2").productId("productId2");
    }

    public static PriceOption getPriceOptionRandomSampleGenerator() {
        return new PriceOption()
            .id(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .productId(UUID.randomUUID().toString());
    }
}
