package com.konsol.core.service.mapper;

import static com.konsol.core.domain.PlaystationDeviceTypeAsserts.*;
import static com.konsol.core.domain.PlaystationDeviceTypeTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlaystationDeviceTypeMapperTest {

    private PlaystationDeviceTypeMapper playstationDeviceTypeMapper;

    @BeforeEach
    void setUp() {
        playstationDeviceTypeMapper = new PlaystationDeviceTypeMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPlaystationDeviceTypeSample1();
        var actual = playstationDeviceTypeMapper.toEntity(playstationDeviceTypeMapper.toDto(expected));
        assertPlaystationDeviceTypeAllPropertiesEquals(expected, actual);
    }
}
