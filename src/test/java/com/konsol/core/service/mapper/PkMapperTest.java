package com.konsol.core.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PkMapperTest {

    private PkMapper pkMapper;

    @BeforeEach
    public void setUp() {
        pkMapper = new PkMapperImpl();
    }
}
