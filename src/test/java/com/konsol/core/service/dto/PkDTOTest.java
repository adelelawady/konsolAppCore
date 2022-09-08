package com.konsol.core.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PkDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PkDTO.class);
        PkDTO pkDTO1 = new PkDTO();
        pkDTO1.setId("id1");
        PkDTO pkDTO2 = new PkDTO();
        assertThat(pkDTO1).isNotEqualTo(pkDTO2);
        pkDTO2.setId(pkDTO1.getId());
        assertThat(pkDTO1).isEqualTo(pkDTO2);
        pkDTO2.setId("id2");
        assertThat(pkDTO1).isNotEqualTo(pkDTO2);
        pkDTO1.setId(null);
        assertThat(pkDTO1).isNotEqualTo(pkDTO2);
    }
}
