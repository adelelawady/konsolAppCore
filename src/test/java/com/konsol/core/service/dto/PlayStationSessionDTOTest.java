package com.konsol.core.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.konsol.core.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PlayStationSessionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayStationSessionDTO.class);
        PlayStationSessionDTO playStationSessionDTO1 = new PlayStationSessionDTO();
        playStationSessionDTO1.setId(1L);
        PlayStationSessionDTO playStationSessionDTO2 = new PlayStationSessionDTO();
        assertThat(playStationSessionDTO1).isNotEqualTo(playStationSessionDTO2);
        playStationSessionDTO2.setId(playStationSessionDTO1.getId());
        assertThat(playStationSessionDTO1).isEqualTo(playStationSessionDTO2);
        playStationSessionDTO2.setId(2L);
        assertThat(playStationSessionDTO1).isNotEqualTo(playStationSessionDTO2);
        playStationSessionDTO1.setId(null);
        assertThat(playStationSessionDTO1).isNotEqualTo(playStationSessionDTO2);
    }
}
