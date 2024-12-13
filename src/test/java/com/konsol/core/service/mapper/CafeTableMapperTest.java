package com.konsol.core.service.mapper;

import static com.konsol.core.domain.CafeTableAsserts.*;
import static com.konsol.core.domain.CafeTableTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CafeTableMapperTest {

    private CafeTableMapper cafeTableMapper;

    @BeforeEach
    void setUp() {
        cafeTableMapper = new CafeTableMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCafeTableSample1();
        var actual = cafeTableMapper.toEntity(cafeTableMapper.toDto(expected));
        assertCafeTableAllPropertiesEquals(expected, actual);
    }
}
