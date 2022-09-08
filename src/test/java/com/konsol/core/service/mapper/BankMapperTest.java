package com.konsol.core.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BankMapperTest {

    private BankMapper bankMapper;

    @BeforeEach
    public void setUp() {
        bankMapper = new BankMapperImpl();
    }
}
