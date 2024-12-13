package com.konsol.core.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlaystationDeviceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlaystationDeviceDTO.class);
        PlaystationDeviceDTO playstationDeviceDTO1 = new PlaystationDeviceDTO();
        playstationDeviceDTO1.setId("id1");
        PlaystationDeviceDTO playstationDeviceDTO2 = new PlaystationDeviceDTO();
        assertThat(playstationDeviceDTO1).isNotEqualTo(playstationDeviceDTO2);
        playstationDeviceDTO2.setId(playstationDeviceDTO1.getId());
        assertThat(playstationDeviceDTO1).isEqualTo(playstationDeviceDTO2);
        playstationDeviceDTO2.setId("id2");
        assertThat(playstationDeviceDTO1).isNotEqualTo(playstationDeviceDTO2);
        playstationDeviceDTO1.setId(null);
        assertThat(playstationDeviceDTO1).isNotEqualTo(playstationDeviceDTO2);
    }
}
