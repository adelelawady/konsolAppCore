package com.konsol.core.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlaystationDeviceTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlaystationDeviceTypeDTO.class);
        PlaystationDeviceTypeDTO playstationDeviceTypeDTO1 = new PlaystationDeviceTypeDTO();
        playstationDeviceTypeDTO1.setId("id1");
        PlaystationDeviceTypeDTO playstationDeviceTypeDTO2 = new PlaystationDeviceTypeDTO();
        assertThat(playstationDeviceTypeDTO1).isNotEqualTo(playstationDeviceTypeDTO2);
        playstationDeviceTypeDTO2.setId(playstationDeviceTypeDTO1.getId());
        assertThat(playstationDeviceTypeDTO1).isEqualTo(playstationDeviceTypeDTO2);
        playstationDeviceTypeDTO2.setId("id2");
        assertThat(playstationDeviceTypeDTO1).isNotEqualTo(playstationDeviceTypeDTO2);
        playstationDeviceTypeDTO1.setId(null);
        assertThat(playstationDeviceTypeDTO1).isNotEqualTo(playstationDeviceTypeDTO2);
    }
}
