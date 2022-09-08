package com.konsol.core.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ItemUnitMapperTest {

    private ItemUnitMapper itemUnitMapper;

    @BeforeEach
    public void setUp() {
        itemUnitMapper = new ItemUnitMapperImpl();
    }
}
