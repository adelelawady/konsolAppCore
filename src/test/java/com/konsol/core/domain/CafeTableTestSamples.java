package com.konsol.core.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CafeTableTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CafeTable getCafeTableSample1() {
        return new CafeTable().id("id1").pk(1L).name("name1").index(1);
    }

    public static CafeTable getCafeTableSample2() {
        return new CafeTable().id("id2").pk(2L).name("name2").index(2);
    }

    public static CafeTable getCafeTableRandomSampleGenerator() {
        return new CafeTable()
            .id(UUID.randomUUID().toString())
            .pk(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .index(intCount.incrementAndGet());
    }
}
