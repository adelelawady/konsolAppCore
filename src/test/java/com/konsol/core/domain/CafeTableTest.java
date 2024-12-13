package com.konsol.core.domain;

import static com.konsol.core.domain.CafeTableTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CafeTableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CafeTable.class);
        CafeTable cafeTable1 = getCafeTableSample1();
        CafeTable cafeTable2 = new CafeTable();
        assertThat(cafeTable1).isNotEqualTo(cafeTable2);

        cafeTable2.setId(cafeTable1.getId());
        assertThat(cafeTable1).isEqualTo(cafeTable2);

        cafeTable2 = getCafeTableSample2();
        assertThat(cafeTable1).isNotEqualTo(cafeTable2);
    }
}
