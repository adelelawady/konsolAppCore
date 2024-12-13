package com.konsol.core.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PlayStationSessionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PlayStationSession getPlayStationSessionSample1() {
        return new PlayStationSession().id(1L).invoiceId("invoiceId1");
    }

    public static PlayStationSession getPlayStationSessionSample2() {
        return new PlayStationSession().id(2L).invoiceId("invoiceId2");
    }

    public static PlayStationSession getPlayStationSessionRandomSampleGenerator() {
        return new PlayStationSession().id(longCount.incrementAndGet()).invoiceId(UUID.randomUUID().toString());
    }
}
