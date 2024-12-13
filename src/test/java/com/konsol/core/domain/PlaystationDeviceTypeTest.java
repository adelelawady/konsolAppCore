package com.konsol.core.domain;

import static com.konsol.core.domain.PlaystationDeviceTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlaystationDeviceTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlaystationDeviceType.class);
        PlaystationDeviceType playstationDeviceType1 = getPlaystationDeviceTypeSample1();
        PlaystationDeviceType playstationDeviceType2 = new PlaystationDeviceType();
        assertThat(playstationDeviceType1).isNotEqualTo(playstationDeviceType2);

        playstationDeviceType2.setId(playstationDeviceType1.getId());
        assertThat(playstationDeviceType1).isEqualTo(playstationDeviceType2);

        playstationDeviceType2 = getPlaystationDeviceTypeSample2();
        assertThat(playstationDeviceType1).isNotEqualTo(playstationDeviceType2);
    }
}
