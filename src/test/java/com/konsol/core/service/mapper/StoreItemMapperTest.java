package com.konsol.core.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StoreItemMapperTest {

    private StoreItemMapper storeItemMapper;

    @BeforeEach
    public void setUp() {
        storeItemMapper = new StoreItemMapperImpl();
    }
}
