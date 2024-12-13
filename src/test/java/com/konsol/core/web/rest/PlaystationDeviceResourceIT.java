package com.konsol.core.web.rest;

import static com.konsol.core.domain.PlaystationDeviceAsserts.*;
import static com.konsol.core.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konsol.core.IntegrationTest;
import com.konsol.core.domain.PlaystationDevice;
import com.konsol.core.repository.PlaystationDeviceRepository;
import com.konsol.core.service.dto.PlaystationDeviceDTO;
import com.konsol.core.service.mapper.PlaystationDeviceMapper;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link PlaystationDeviceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlaystationDeviceResourceIT {

    private static final Long DEFAULT_PK = 1L;
    private static final Long UPDATED_PK = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_INDEX = 1;
    private static final Integer UPDATED_INDEX = 2;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/playstation-devices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PlaystationDeviceRepository playstationDeviceRepository;

    @Autowired
    private PlaystationDeviceMapper playstationDeviceMapper;

    @Autowired
    private MockMvc restPlaystationDeviceMockMvc;

    private PlaystationDevice playstationDevice;

    private PlaystationDevice insertedPlaystationDevice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlaystationDevice createEntity() {
        return new PlaystationDevice().pk(DEFAULT_PK).name(DEFAULT_NAME).index(DEFAULT_INDEX).active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlaystationDevice createUpdatedEntity() {
        return new PlaystationDevice().pk(UPDATED_PK).name(UPDATED_NAME).index(UPDATED_INDEX).active(UPDATED_ACTIVE);
    }

    @BeforeEach
    public void initTest() {
        playstationDevice = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPlaystationDevice != null) {
            playstationDeviceRepository.delete(insertedPlaystationDevice);
            insertedPlaystationDevice = null;
        }
    }

    @Test
    void createPlaystationDevice() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PlaystationDevice
        PlaystationDeviceDTO playstationDeviceDTO = playstationDeviceMapper.toDto(playstationDevice);
        var returnedPlaystationDeviceDTO = om.readValue(
            restPlaystationDeviceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(playstationDeviceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PlaystationDeviceDTO.class
        );

        // Validate the PlaystationDevice in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPlaystationDevice = playstationDeviceMapper.toEntity(returnedPlaystationDeviceDTO);
        assertPlaystationDeviceUpdatableFieldsEquals(returnedPlaystationDevice, getPersistedPlaystationDevice(returnedPlaystationDevice));

        insertedPlaystationDevice = returnedPlaystationDevice;
    }

    @Test
    void createPlaystationDeviceWithExistingId() throws Exception {
        // Create the PlaystationDevice with an existing ID
        playstationDevice.setId("existing_id");
        PlaystationDeviceDTO playstationDeviceDTO = playstationDeviceMapper.toDto(playstationDevice);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlaystationDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(playstationDeviceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlaystationDevice in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkPkIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        playstationDevice.setPk(null);

        // Create the PlaystationDevice, which fails.
        PlaystationDeviceDTO playstationDeviceDTO = playstationDeviceMapper.toDto(playstationDevice);

        restPlaystationDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(playstationDeviceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        playstationDevice.setName(null);

        // Create the PlaystationDevice, which fails.
        PlaystationDeviceDTO playstationDeviceDTO = playstationDeviceMapper.toDto(playstationDevice);

        restPlaystationDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(playstationDeviceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkIndexIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        playstationDevice.setIndex(null);

        // Create the PlaystationDevice, which fails.
        PlaystationDeviceDTO playstationDeviceDTO = playstationDeviceMapper.toDto(playstationDevice);

        restPlaystationDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(playstationDeviceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        playstationDevice.setActive(null);

        // Create the PlaystationDevice, which fails.
        PlaystationDeviceDTO playstationDeviceDTO = playstationDeviceMapper.toDto(playstationDevice);

        restPlaystationDeviceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(playstationDeviceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllPlaystationDevices() throws Exception {
        // Initialize the database
        insertedPlaystationDevice = playstationDeviceRepository.save(playstationDevice);

        // Get all the playstationDeviceList
        restPlaystationDeviceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playstationDevice.getId())))
            .andExpect(jsonPath("$.[*].pk").value(hasItem(DEFAULT_PK.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    void getPlaystationDevice() throws Exception {
        // Initialize the database
        insertedPlaystationDevice = playstationDeviceRepository.save(playstationDevice);

        // Get the playstationDevice
        restPlaystationDeviceMockMvc
            .perform(get(ENTITY_API_URL_ID, playstationDevice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(playstationDevice.getId()))
            .andExpect(jsonPath("$.pk").value(DEFAULT_PK.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.index").value(DEFAULT_INDEX))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    void getNonExistingPlaystationDevice() throws Exception {
        // Get the playstationDevice
        restPlaystationDeviceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingPlaystationDevice() throws Exception {
        // Initialize the database
        insertedPlaystationDevice = playstationDeviceRepository.save(playstationDevice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the playstationDevice
        PlaystationDevice updatedPlaystationDevice = playstationDeviceRepository.findById(playstationDevice.getId()).orElseThrow();
        updatedPlaystationDevice.pk(UPDATED_PK).name(UPDATED_NAME).index(UPDATED_INDEX).active(UPDATED_ACTIVE);
        PlaystationDeviceDTO playstationDeviceDTO = playstationDeviceMapper.toDto(updatedPlaystationDevice);

        restPlaystationDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playstationDeviceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(playstationDeviceDTO))
            )
            .andExpect(status().isOk());

        // Validate the PlaystationDevice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPlaystationDeviceToMatchAllProperties(updatedPlaystationDevice);
    }

    @Test
    void putNonExistingPlaystationDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        playstationDevice.setId(UUID.randomUUID().toString());

        // Create the PlaystationDevice
        PlaystationDeviceDTO playstationDeviceDTO = playstationDeviceMapper.toDto(playstationDevice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaystationDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playstationDeviceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(playstationDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlaystationDevice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPlaystationDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        playstationDevice.setId(UUID.randomUUID().toString());

        // Create the PlaystationDevice
        PlaystationDeviceDTO playstationDeviceDTO = playstationDeviceMapper.toDto(playstationDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaystationDeviceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(playstationDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlaystationDevice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPlaystationDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        playstationDevice.setId(UUID.randomUUID().toString());

        // Create the PlaystationDevice
        PlaystationDeviceDTO playstationDeviceDTO = playstationDeviceMapper.toDto(playstationDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaystationDeviceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(playstationDeviceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlaystationDevice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePlaystationDeviceWithPatch() throws Exception {
        // Initialize the database
        insertedPlaystationDevice = playstationDeviceRepository.save(playstationDevice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the playstationDevice using partial update
        PlaystationDevice partialUpdatedPlaystationDevice = new PlaystationDevice();
        partialUpdatedPlaystationDevice.setId(playstationDevice.getId());

        partialUpdatedPlaystationDevice.pk(UPDATED_PK).name(UPDATED_NAME).active(UPDATED_ACTIVE);

        restPlaystationDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlaystationDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlaystationDevice))
            )
            .andExpect(status().isOk());

        // Validate the PlaystationDevice in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlaystationDeviceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPlaystationDevice, playstationDevice),
            getPersistedPlaystationDevice(playstationDevice)
        );
    }

    @Test
    void fullUpdatePlaystationDeviceWithPatch() throws Exception {
        // Initialize the database
        insertedPlaystationDevice = playstationDeviceRepository.save(playstationDevice);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the playstationDevice using partial update
        PlaystationDevice partialUpdatedPlaystationDevice = new PlaystationDevice();
        partialUpdatedPlaystationDevice.setId(playstationDevice.getId());

        partialUpdatedPlaystationDevice.pk(UPDATED_PK).name(UPDATED_NAME).index(UPDATED_INDEX).active(UPDATED_ACTIVE);

        restPlaystationDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlaystationDevice.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlaystationDevice))
            )
            .andExpect(status().isOk());

        // Validate the PlaystationDevice in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlaystationDeviceUpdatableFieldsEquals(
            partialUpdatedPlaystationDevice,
            getPersistedPlaystationDevice(partialUpdatedPlaystationDevice)
        );
    }

    @Test
    void patchNonExistingPlaystationDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        playstationDevice.setId(UUID.randomUUID().toString());

        // Create the PlaystationDevice
        PlaystationDeviceDTO playstationDeviceDTO = playstationDeviceMapper.toDto(playstationDevice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaystationDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, playstationDeviceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(playstationDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlaystationDevice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPlaystationDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        playstationDevice.setId(UUID.randomUUID().toString());

        // Create the PlaystationDevice
        PlaystationDeviceDTO playstationDeviceDTO = playstationDeviceMapper.toDto(playstationDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaystationDeviceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(playstationDeviceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlaystationDevice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPlaystationDevice() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        playstationDevice.setId(UUID.randomUUID().toString());

        // Create the PlaystationDevice
        PlaystationDeviceDTO playstationDeviceDTO = playstationDeviceMapper.toDto(playstationDevice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaystationDeviceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(playstationDeviceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlaystationDevice in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePlaystationDevice() throws Exception {
        // Initialize the database
        insertedPlaystationDevice = playstationDeviceRepository.save(playstationDevice);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the playstationDevice
        restPlaystationDeviceMockMvc
            .perform(delete(ENTITY_API_URL_ID, playstationDevice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return playstationDeviceRepository.count();
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

    protected PlaystationDevice getPersistedPlaystationDevice(PlaystationDevice playstationDevice) {
        return playstationDeviceRepository.findById(playstationDevice.getId()).orElseThrow();
    }

    protected void assertPersistedPlaystationDeviceToMatchAllProperties(PlaystationDevice expectedPlaystationDevice) {
        assertPlaystationDeviceAllPropertiesEquals(expectedPlaystationDevice, getPersistedPlaystationDevice(expectedPlaystationDevice));
    }

    protected void assertPersistedPlaystationDeviceToMatchUpdatableProperties(PlaystationDevice expectedPlaystationDevice) {
        assertPlaystationDeviceAllUpdatablePropertiesEquals(
            expectedPlaystationDevice,
            getPersistedPlaystationDevice(expectedPlaystationDevice)
        );
    }
}
