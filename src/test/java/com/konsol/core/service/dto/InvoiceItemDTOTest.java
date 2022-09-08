package com.konsol.core.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvoiceItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvoiceItemDTO.class);
        InvoiceItemDTO invoiceItemDTO1 = new InvoiceItemDTO();
        invoiceItemDTO1.setId("id1");
        InvoiceItemDTO invoiceItemDTO2 = new InvoiceItemDTO();
        assertThat(invoiceItemDTO1).isNotEqualTo(invoiceItemDTO2);
        invoiceItemDTO2.setId(invoiceItemDTO1.getId());
        assertThat(invoiceItemDTO1).isEqualTo(invoiceItemDTO2);
        invoiceItemDTO2.setId("id2");
        assertThat(invoiceItemDTO1).isNotEqualTo(invoiceItemDTO2);
        invoiceItemDTO1.setId(null);
        assertThat(invoiceItemDTO1).isNotEqualTo(invoiceItemDTO2);
    }
}
