package com.konsol.core.domain;

import static com.konsol.core.domain.PlaystationDeviceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlaystationDeviceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlaystationDevice.class);
        PlaystationDevice playstationDevice1 = getPlaystationDeviceSample1();
        PlaystationDevice playstationDevice2 = new PlaystationDevice();
        assertThat(playstationDevice1).isNotEqualTo(playstationDevice2);

        playstationDevice2.setId(playstationDevice1.getId());
        assertThat(playstationDevice1).isEqualTo(playstationDevice2);

        playstationDevice2 = getPlaystationDeviceSample2();
        assertThat(playstationDevice1).isNotEqualTo(playstationDevice2);
    }
}
