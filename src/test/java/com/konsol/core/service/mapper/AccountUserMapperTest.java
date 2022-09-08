package com.konsol.core.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountUserMapperTest {

    private AccountUserMapper accountUserMapper;

    @BeforeEach
    public void setUp() {
        accountUserMapper = new AccountUserMapperImpl();
    }
}
