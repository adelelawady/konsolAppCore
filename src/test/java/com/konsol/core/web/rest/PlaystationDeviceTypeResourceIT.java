package com.konsol.core.web.rest;

import static com.konsol.core.domain.PlaystationDeviceTypeAsserts.*;
import static com.konsol.core.web.rest.TestUtil.createUpdateProxyForBean;
import static com.konsol.core.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konsol.core.IntegrationTest;
import com.konsol.core.domain.PlaystationDeviceType;
import com.konsol.core.repository.PlaystationDeviceTypeRepository;
import com.konsol.core.service.dto.PlaystationDeviceTypeDTO;
import com.konsol.core.service.mapper.PlaystationDeviceTypeMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link PlaystationDeviceTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlaystationDeviceTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_DEFAULT_MAIN_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEFAULT_MAIN_PRICE = new BigDecimal(2);

    private static final String DEFAULT_PRODUCT_ID = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/playstation-device-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PlaystationDeviceTypeRepository playstationDeviceTypeRepository;

    @Autowired
    private PlaystationDeviceTypeMapper playstationDeviceTypeMapper;

    @Autowired
    private MockMvc restPlaystationDeviceTypeMockMvc;

    private PlaystationDeviceType playstationDeviceType;

    private PlaystationDeviceType insertedPlaystationDeviceType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlaystationDeviceType createEntity() {
        return new PlaystationDeviceType().name(DEFAULT_NAME).defaultMainPrice(DEFAULT_DEFAULT_MAIN_PRICE).productId(DEFAULT_PRODUCT_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlaystationDeviceType createUpdatedEntity() {
        return new PlaystationDeviceType().name(UPDATED_NAME).defaultMainPrice(UPDATED_DEFAULT_MAIN_PRICE).productId(UPDATED_PRODUCT_ID);
    }

    @BeforeEach
    public void initTest() {
        playstationDeviceType = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPlaystationDeviceType != null) {
            playstationDeviceTypeRepository.delete(insertedPlaystationDeviceType);
            insertedPlaystationDeviceType = null;
        }
    }

    @Test
    void createPlaystationDeviceType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PlaystationDeviceType
        PlaystationDeviceTypeDTO playstationDeviceTypeDTO = playstationDeviceTypeMapper.toDto(playstationDeviceType);
        var returnedPlaystationDeviceTypeDTO = om.readValue(
            restPlaystationDeviceTypeMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(playstationDeviceTypeDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PlaystationDeviceTypeDTO.class
        );

        // Validate the PlaystationDeviceType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPlaystationDeviceType = playstationDeviceTypeMapper.toEntity(returnedPlaystationDeviceTypeDTO);
        assertPlaystationDeviceTypeUpdatableFieldsEquals(
            returnedPlaystationDeviceType,
            getPersistedPlaystationDeviceType(returnedPlaystationDeviceType)
        );

        insertedPlaystationDeviceType = returnedPlaystationDeviceType;
    }

    @Test
    void createPlaystationDeviceTypeWithExistingId() throws Exception {
        // Create the PlaystationDeviceType with an existing ID
        playstationDeviceType.setId("existing_id");
        PlaystationDeviceTypeDTO playstationDeviceTypeDTO = playstationDeviceTypeMapper.toDto(playstationDeviceType);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlaystationDeviceTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(playstationDeviceTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlaystationDeviceType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        playstationDeviceType.setName(null);

        // Create the PlaystationDeviceType, which fails.
        PlaystationDeviceTypeDTO playstationDeviceTypeDTO = playstationDeviceTypeMapper.toDto(playstationDeviceType);

        restPlaystationDeviceTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(playstationDeviceTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkDefaultMainPriceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        playstationDeviceType.setDefaultMainPrice(null);

        // Create the PlaystationDeviceType, which fails.
        PlaystationDeviceTypeDTO playstationDeviceTypeDTO = playstationDeviceTypeMapper.toDto(playstationDeviceType);

        restPlaystationDeviceTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(playstationDeviceTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkProductIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        playstationDeviceType.setProductId(null);

        // Create the PlaystationDeviceType, which fails.
        PlaystationDeviceTypeDTO playstationDeviceTypeDTO = playstationDeviceTypeMapper.toDto(playstationDeviceType);

        restPlaystationDeviceTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(playstationDeviceTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllPlaystationDeviceTypes() throws Exception {
        // Initialize the database
        insertedPlaystationDeviceType = playstationDeviceTypeRepository.save(playstationDeviceType);

        // Get all the playstationDeviceTypeList
        restPlaystationDeviceTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(playstationDeviceType.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].defaultMainPrice").value(hasItem(sameNumber(DEFAULT_DEFAULT_MAIN_PRICE))))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID)));
    }

    @Test
    void getPlaystationDeviceType() throws Exception {
        // Initialize the database
        insertedPlaystationDeviceType = playstationDeviceTypeRepository.save(playstationDeviceType);

        // Get the playstationDeviceType
        restPlaystationDeviceTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, playstationDeviceType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(playstationDeviceType.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.defaultMainPrice").value(sameNumber(DEFAULT_DEFAULT_MAIN_PRICE)))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID));
    }

    @Test
    void getNonExistingPlaystationDeviceType() throws Exception {
        // Get the playstationDeviceType
        restPlaystationDeviceTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingPlaystationDeviceType() throws Exception {
        // Initialize the database
        insertedPlaystationDeviceType = playstationDeviceTypeRepository.save(playstationDeviceType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the playstationDeviceType
        PlaystationDeviceType updatedPlaystationDeviceType = playstationDeviceTypeRepository
            .findById(playstationDeviceType.getId())
            .orElseThrow();
        updatedPlaystationDeviceType.name(UPDATED_NAME).defaultMainPrice(UPDATED_DEFAULT_MAIN_PRICE).productId(UPDATED_PRODUCT_ID);
        PlaystationDeviceTypeDTO playstationDeviceTypeDTO = playstationDeviceTypeMapper.toDto(updatedPlaystationDeviceType);

        restPlaystationDeviceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playstationDeviceTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(playstationDeviceTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the PlaystationDeviceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPlaystationDeviceTypeToMatchAllProperties(updatedPlaystationDeviceType);
    }

    @Test
    void putNonExistingPlaystationDeviceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        playstationDeviceType.setId(UUID.randomUUID().toString());

        // Create the PlaystationDeviceType
        PlaystationDeviceTypeDTO playstationDeviceTypeDTO = playstationDeviceTypeMapper.toDto(playstationDeviceType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaystationDeviceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, playstationDeviceTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(playstationDeviceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlaystationDeviceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPlaystationDeviceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        playstationDeviceType.setId(UUID.randomUUID().toString());

        // Create the PlaystationDeviceType
        PlaystationDeviceTypeDTO playstationDeviceTypeDTO = playstationDeviceTypeMapper.toDto(playstationDeviceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaystationDeviceTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(playstationDeviceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlaystationDeviceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPlaystationDeviceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        playstationDeviceType.setId(UUID.randomUUID().toString());

        // Create the PlaystationDeviceType
        PlaystationDeviceTypeDTO playstationDeviceTypeDTO = playstationDeviceTypeMapper.toDto(playstationDeviceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaystationDeviceTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(playstationDeviceTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlaystationDeviceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePlaystationDeviceTypeWithPatch() throws Exception {
        // Initialize the database
        insertedPlaystationDeviceType = playstationDeviceTypeRepository.save(playstationDeviceType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the playstationDeviceType using partial update
        PlaystationDeviceType partialUpdatedPlaystationDeviceType = new PlaystationDeviceType();
        partialUpdatedPlaystationDeviceType.setId(playstationDeviceType.getId());

        partialUpdatedPlaystationDeviceType.name(UPDATED_NAME);

        restPlaystationDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlaystationDeviceType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlaystationDeviceType))
            )
            .andExpect(status().isOk());

        // Validate the PlaystationDeviceType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlaystationDeviceTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPlaystationDeviceType, playstationDeviceType),
            getPersistedPlaystationDeviceType(playstationDeviceType)
        );
    }

    @Test
    void fullUpdatePlaystationDeviceTypeWithPatch() throws Exception {
        // Initialize the database
        insertedPlaystationDeviceType = playstationDeviceTypeRepository.save(playstationDeviceType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the playstationDeviceType using partial update
        PlaystationDeviceType partialUpdatedPlaystationDeviceType = new PlaystationDeviceType();
        partialUpdatedPlaystationDeviceType.setId(playstationDeviceType.getId());

        partialUpdatedPlaystationDeviceType.name(UPDATED_NAME).defaultMainPrice(UPDATED_DEFAULT_MAIN_PRICE).productId(UPDATED_PRODUCT_ID);

        restPlaystationDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlaystationDeviceType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlaystationDeviceType))
            )
            .andExpect(status().isOk());

        // Validate the PlaystationDeviceType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlaystationDeviceTypeUpdatableFieldsEquals(
            partialUpdatedPlaystationDeviceType,
            getPersistedPlaystationDeviceType(partialUpdatedPlaystationDeviceType)
        );
    }

    @Test
    void patchNonExistingPlaystationDeviceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        playstationDeviceType.setId(UUID.randomUUID().toString());

        // Create the PlaystationDeviceType
        PlaystationDeviceTypeDTO playstationDeviceTypeDTO = playstationDeviceTypeMapper.toDto(playstationDeviceType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaystationDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, playstationDeviceTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(playstationDeviceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlaystationDeviceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPlaystationDeviceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        playstationDeviceType.setId(UUID.randomUUID().toString());

        // Create the PlaystationDeviceType
        PlaystationDeviceTypeDTO playstationDeviceTypeDTO = playstationDeviceTypeMapper.toDto(playstationDeviceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaystationDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(playstationDeviceTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlaystationDeviceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPlaystationDeviceType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        playstationDeviceType.setId(UUID.randomUUID().toString());

        // Create the PlaystationDeviceType
        PlaystationDeviceTypeDTO playstationDeviceTypeDTO = playstationDeviceTypeMapper.toDto(playstationDeviceType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlaystationDeviceTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(playstationDeviceTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlaystationDeviceType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePlaystationDeviceType() throws Exception {
        // Initialize the database
        insertedPlaystationDeviceType = playstationDeviceTypeRepository.save(playstationDeviceType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the playstationDeviceType
        restPlaystationDeviceTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, playstationDeviceType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return playstationDeviceTypeRepository.count();
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

    protected PlaystationDeviceType getPersistedPlaystationDeviceType(PlaystationDeviceType playstationDeviceType) {
        return playstationDeviceTypeRepository.findById(playstationDeviceType.getId()).orElseThrow();
    }

    protected void assertPersistedPlaystationDeviceTypeToMatchAllProperties(PlaystationDeviceType expectedPlaystationDeviceType) {
        assertPlaystationDeviceTypeAllPropertiesEquals(
            expectedPlaystationDeviceType,
            getPersistedPlaystationDeviceType(expectedPlaystationDeviceType)
        );
    }

    protected void assertPersistedPlaystationDeviceTypeToMatchUpdatableProperties(PlaystationDeviceType expectedPlaystationDeviceType) {
        assertPlaystationDeviceTypeAllUpdatablePropertiesEquals(
            expectedPlaystationDeviceType,
            getPersistedPlaystationDeviceType(expectedPlaystationDeviceType)
        );
    }
}
