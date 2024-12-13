package com.konsol.core.web.rest;

import static com.konsol.core.domain.PlayStationSessionAsserts.*;
import static com.konsol.core.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konsol.core.IntegrationTest;
import com.konsol.core.domain.PlayStationSession;
import com.konsol.core.repository.PlayStationSessionRepository;
import com.konsol.core.service.dto.PlayStationSessionDTO;
import com.konsol.core.service.mapper.PlayStationSessionMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link PlayStationSessionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlayStationSessionResourceIT {

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_INVOICE_ID = "AAAAAAAAAA";
    private static final String UPDATED_INVOICE_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/play-station-sessions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PlayStationSessionRepository playStationSessionRepository;

    @Autowired
    private PlayStationSessionMapper playStationSessionMapper;

    @Autowired
    private MockMvc restPlayStationSessionMockMvc;

    private PlayStationSession playStationSession;

    private PlayStationSession insertedPlayStationSession;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayStationSession createEntity() {
        return new PlayStationSession()
            .active(DEFAULT_ACTIVE)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .invoiceId(DEFAULT_INVOICE_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlayStationSession createUpdatedEntity() {
        return new PlayStationSession()
            .active(UPDATED_ACTIVE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .invoiceId(UPDATED_INVOICE_ID);
    }

    @BeforeEach
    public void initTest() {
        playStationSession = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPlayStationSession != null) {
            playStationSessionRepository.delete(insertedPlayStationSession);
            insertedPlayStationSession = null;
        }
    }

    @Test
    void createPlayStationSession() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PlayStationSession
        PlayStationSessionDTO playStationSessionDTO = playStationSessionMapper.toDto(playStationSession);
        var returnedPlayStationSessionDTO = om.readValue(
            restPlayStationSessionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(playStationSessionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PlayStationSessionDTO.class
        );

        // Validate the PlayStationSession in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPlayStationSession = playStationSessionMapper.toEntity(returnedPlayStationSessionDTO);
        assertPlayStationSessionUpdatableFieldsEquals(
            returnedPlayStationSession,
            getPersistedPlayStationSession(returnedPlayStationSession)
        );

        insertedPlayStationSession = returnedPlayStationSession;
    }

    @Test
    void createPlayStationSessionWithExistingId() throws Exception {
        // Create the PlayStationSession with an existing ID
        playStationSession.setId(1L);
        PlayStationSessionDTO playStationSessionDTO = playStationSessionMapper.toDto(playStationSession);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayStationSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(playStationSessionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlayStationSession in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        playStationSession.setActive(null);

        // Create the PlayStationSession, which fails.
        PlayStationSessionDTO playStationSessionDTO = playStationSessionMapper.toDto(playStationSession);

        restPlayStationSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(playStationSessionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkStartTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        playStationSession.setStartTime(null);

        // Create the PlayStationSession, which fails.
        PlayStationSessionDTO playStationSessionDTO = playStationSessionMapper.toDto(playStationSession);

        restPlayStationSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(playStationSessionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkInvoiceIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        playStationSession.setInvoiceId(null);

        // Create the PlayStationSession, which fails.
        PlayStationSessionDTO playStationSessionDTO = playStationSessionMapper.toDto(playStationSession);

        restPlayStationSessionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(playStationSessionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllPlayStationSessions() throws Exception {
        // Initialize the database
        insertedPlayStationSession = playStationSessionRepository.save(playStationSession);

        // Get all the playStationSessionList
        restPlayStationSessionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playStationSession.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())))
            .andExpect(jsonPath("$.[*].invoiceId").value(hasItem(DEFAULT_INVOICE_ID)));
    }

    @Test
    void getPlayStationSession() throws Exception {
        // Initialize the database
        insertedPlayStationSession = playStationSessionRepository.save(playStationSession);

        // Get the playStationSession
        restPlayStationSessionMockMvc
            .perform(get(ENTITY_API_URL_ID, playStationSession.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(playStationSession.getId().intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()))
            .andExpect(jsonPath("$.invoiceId").value(DEFAULT_INVOICE_ID));
    }

    @Test
    void getNonExistingPlayStationSession() throws Exception {
        // Get the playStationSession
        restPlayStationSessionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingPlayStationSession() throws Exception {
        // Initialize the database
        insertedPlayStationSession = playStationSessionRepository.save(playStationSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the playStationSession
        PlayStationSession updatedPlayStationSession = playStationSessionRepository.findById(playStationSession.getId()).orElseThrow();
        updatedPlayStationSession
            .active(UPDATED_ACTIVE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .invoiceId(UPDATED_INVOICE_ID);
        PlayStationSessionDTO playStationSessionDTO = playStationSessionMapper.toDto(updatedPlayStationSession);

        restPlayStationSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playStationSessionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(playStationSessionDTO))
            )
            .andExpect(status().isOk());

        // Validate the PlayStationSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPlayStationSessionToMatchAllProperties(updatedPlayStationSession);
    }

    @Test
    void putNonExistingPlayStationSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        playStationSession.setId(longCount.incrementAndGet());

        // Create the PlayStationSession
        PlayStationSessionDTO playStationSessionDTO = playStationSessionMapper.toDto(playStationSession);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayStationSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playStationSessionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(playStationSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayStationSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPlayStationSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        playStationSession.setId(longCount.incrementAndGet());

        // Create the PlayStationSession
        PlayStationSessionDTO playStationSessionDTO = playStationSessionMapper.toDto(playStationSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayStationSessionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(playStationSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayStationSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPlayStationSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        playStationSession.setId(longCount.incrementAndGet());

        // Create the PlayStationSession
        PlayStationSessionDTO playStationSessionDTO = playStationSessionMapper.toDto(playStationSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayStationSessionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(playStationSessionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayStationSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePlayStationSessionWithPatch() throws Exception {
        // Initialize the database
        insertedPlayStationSession = playStationSessionRepository.save(playStationSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the playStationSession using partial update
        PlayStationSession partialUpdatedPlayStationSession = new PlayStationSession();
        partialUpdatedPlayStationSession.setId(playStationSession.getId());

        partialUpdatedPlayStationSession.startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);

        restPlayStationSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayStationSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlayStationSession))
            )
            .andExpect(status().isOk());

        // Validate the PlayStationSession in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlayStationSessionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPlayStationSession, playStationSession),
            getPersistedPlayStationSession(playStationSession)
        );
    }

    @Test
    void fullUpdatePlayStationSessionWithPatch() throws Exception {
        // Initialize the database
        insertedPlayStationSession = playStationSessionRepository.save(playStationSession);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the playStationSession using partial update
        PlayStationSession partialUpdatedPlayStationSession = new PlayStationSession();
        partialUpdatedPlayStationSession.setId(playStationSession.getId());

        partialUpdatedPlayStationSession
            .active(UPDATED_ACTIVE)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .invoiceId(UPDATED_INVOICE_ID);

        restPlayStationSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlayStationSession.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlayStationSession))
            )
            .andExpect(status().isOk());

        // Validate the PlayStationSession in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlayStationSessionUpdatableFieldsEquals(
            partialUpdatedPlayStationSession,
            getPersistedPlayStationSession(partialUpdatedPlayStationSession)
        );
    }

    @Test
    void patchNonExistingPlayStationSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        playStationSession.setId(longCount.incrementAndGet());

        // Create the PlayStationSession
        PlayStationSessionDTO playStationSessionDTO = playStationSessionMapper.toDto(playStationSession);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayStationSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, playStationSessionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(playStationSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayStationSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPlayStationSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        playStationSession.setId(longCount.incrementAndGet());

        // Create the PlayStationSession
        PlayStationSessionDTO playStationSessionDTO = playStationSessionMapper.toDto(playStationSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayStationSessionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(playStationSessionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlayStationSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPlayStationSession() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        playStationSession.setId(longCount.incrementAndGet());

        // Create the PlayStationSession
        PlayStationSessionDTO playStationSessionDTO = playStationSessionMapper.toDto(playStationSession);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlayStationSessionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(playStationSessionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlayStationSession in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePlayStationSession() throws Exception {
        // Initialize the database
        insertedPlayStationSession = playStationSessionRepository.save(playStationSession);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the playStationSession
        restPlayStationSessionMockMvc
            .perform(delete(ENTITY_API_URL_ID, playStationSession.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return playStationSessionRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected PlayStationSession getPersistedPlayStationSession(PlayStationSession playStationSession) {
        return playStationSessionRepository.findById(playStationSession.getId()).orElseThrow();
    }

    protected void assertPersistedPlayStationSessionToMatchAllProperties(PlayStationSession expectedPlayStationSession) {
        assertPlayStationSessionAllPropertiesEquals(expectedPlayStationSession, getPersistedPlayStationSession(expectedPlayStationSession));
    }

    protected void assertPersistedPlayStationSessionToMatchUpdatableProperties(PlayStationSession expectedPlayStationSession) {
        assertPlayStationSessionAllUpdatablePropertiesEquals(
            expectedPlayStationSession,
            getPersistedPlayStationSession(expectedPlayStationSession)
        );
    }
}
