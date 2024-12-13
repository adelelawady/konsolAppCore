package com.konsol.core.service.mapper;

import static com.konsol.core.domain.PlayStationSessionAsserts.*;
import static com.konsol.core.domain.PlayStationSessionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlayStationSessionMapperTest {

    private PlayStationSessionMapper playStationSessionMapper;

    @BeforeEach
    void setUp() {
        playStationSessionMapper = new PlayStationSessionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPlayStationSessionSample1();
        var actual = playStationSessionMapper.toEntity(playStationSessionMapper.toDto(expected));
        assertPlayStationSessionAllPropertiesEquals(expected, actual);
    }
}
