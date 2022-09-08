package com.konsol.core.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountUser.class);
        AccountUser accountUser1 = new AccountUser();
        accountUser1.setId("id1");
        AccountUser accountUser2 = new AccountUser();
        accountUser2.setId(accountUser1.getId());
        assertThat(accountUser1).isEqualTo(accountUser2);
        accountUser2.setId("id2");
        assertThat(accountUser1).isNotEqualTo(accountUser2);
        accountUser1.setId(null);
        assertThat(accountUser1).isNotEqualTo(accountUser2);
    }
}
