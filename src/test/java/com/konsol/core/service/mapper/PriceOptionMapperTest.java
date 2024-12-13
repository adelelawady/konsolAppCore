package com.konsol.core.service.mapper;

import static com.konsol.core.domain.PriceOptionAsserts.*;
import static com.konsol.core.domain.PriceOptionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PriceOptionMapperTest {

    private PriceOptionMapper priceOptionMapper;

    @BeforeEach
    void setUp() {
        priceOptionMapper = new PriceOptionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPriceOptionSample1();
        var actual = priceOptionMapper.toEntity(priceOptionMapper.toDto(expected));
        assertPriceOptionAllPropertiesEquals(expected, actual);
    }
}
