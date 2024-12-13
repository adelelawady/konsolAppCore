package com.konsol.core.domain;

import java.util.UUID;

public class PlaystationDeviceTypeTestSamples {

    public static PlaystationDeviceType getPlaystationDeviceTypeSample1() {
        return new PlaystationDeviceType().id("id1").name("name1").productId("productId1");
    }

    public static PlaystationDeviceType getPlaystationDeviceTypeSample2() {
        return new PlaystationDeviceType().id("id2").name("name2").productId("productId2");
    }

    public static PlaystationDeviceType getPlaystationDeviceTypeRandomSampleGenerator() {
        return new PlaystationDeviceType()
            .id(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .productId(UUID.randomUUID().toString());
    }
}
