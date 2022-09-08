package com.konsol.core.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountUserDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountUserDTO.class);
        AccountUserDTO accountUserDTO1 = new AccountUserDTO();
        accountUserDTO1.setId("id1");
        AccountUserDTO accountUserDTO2 = new AccountUserDTO();
        assertThat(accountUserDTO1).isNotEqualTo(accountUserDTO2);
        accountUserDTO2.setId(accountUserDTO1.getId());
        assertThat(accountUserDTO1).isEqualTo(accountUserDTO2);
        accountUserDTO2.setId("id2");
        assertThat(accountUserDTO1).isNotEqualTo(accountUserDTO2);
        accountUserDTO1.setId(null);
        assertThat(accountUserDTO1).isNotEqualTo(accountUserDTO2);
    }
}
