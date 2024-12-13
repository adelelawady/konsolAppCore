package com.konsol.core.domain;

import static com.konsol.core.domain.PlayStationSessionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlayStationSessionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayStationSession.class);
        PlayStationSession playStationSession1 = getPlayStationSessionSample1();
        PlayStationSession playStationSession2 = new PlayStationSession();
        assertThat(playStationSession1).isNotEqualTo(playStationSession2);

        playStationSession2.setId(playStationSession1.getId());
        assertThat(playStationSession1).isEqualTo(playStationSession2);

        playStationSession2 = getPlayStationSessionSample2();
        assertThat(playStationSession1).isNotEqualTo(playStationSession2);
    }
}
