package com.konsol.core.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MoneyMapperTest {

    private MoneyMapper moneyMapper;

    @BeforeEach
    public void setUp() {
        moneyMapper = new MoneyMapperImpl();
    }
}
