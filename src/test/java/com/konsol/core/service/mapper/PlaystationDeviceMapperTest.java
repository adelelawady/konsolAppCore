package com.konsol.core.service.mapper;

import static com.konsol.core.domain.PlaystationDeviceAsserts.*;
import static com.konsol.core.domain.PlaystationDeviceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlaystationDeviceMapperTest {

    private PlaystationDeviceMapper playstationDeviceMapper;

    @BeforeEach
    void setUp() {
        playstationDeviceMapper = new PlaystationDeviceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPlaystationDeviceSample1();
        var actual = playstationDeviceMapper.toEntity(playstationDeviceMapper.toDto(expected));
        assertPlaystationDeviceAllPropertiesEquals(expected, actual);
    }
}
