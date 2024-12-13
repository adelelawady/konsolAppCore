package com.konsol.core.web.rest;

import static com.konsol.core.domain.CafeTableAsserts.*;
import static com.konsol.core.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konsol.core.IntegrationTest;
import com.konsol.core.domain.CafeTable;
import com.konsol.core.repository.CafeTableRepository;
import com.konsol.core.service.dto.CafeTableDTO;
import com.konsol.core.service.mapper.CafeTableMapper;
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
 * Integration tests for the {@link CafeTableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CafeTableResourceIT {

    private static final Long DEFAULT_PK = 1L;
    private static final Long UPDATED_PK = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_INDEX = 1;
    private static final Integer UPDATED_INDEX = 2;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/cafe-tables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CafeTableRepository cafeTableRepository;

    @Autowired
    private CafeTableMapper cafeTableMapper;

    @Autowired
    private MockMvc restCafeTableMockMvc;

    private CafeTable cafeTable;

    private CafeTable insertedCafeTable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CafeTable createEntity() {
        return new CafeTable().pk(DEFAULT_PK).name(DEFAULT_NAME).index(DEFAULT_INDEX).active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CafeTable createUpdatedEntity() {
        return new CafeTable().pk(UPDATED_PK).name(UPDATED_NAME).index(UPDATED_INDEX).active(UPDATED_ACTIVE);
    }

    @BeforeEach
    public void initTest() {
        cafeTable = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedCafeTable != null) {
            cafeTableRepository.delete(insertedCafeTable);
            insertedCafeTable = null;
        }
    }

    @Test
    void createCafeTable() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CafeTable
        CafeTableDTO cafeTableDTO = cafeTableMapper.toDto(cafeTable);
        var returnedCafeTableDTO = om.readValue(
            restCafeTableMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cafeTableDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CafeTableDTO.class
        );

        // Validate the CafeTable in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCafeTable = cafeTableMapper.toEntity(returnedCafeTableDTO);
        assertCafeTableUpdatableFieldsEquals(returnedCafeTable, getPersistedCafeTable(returnedCafeTable));

        insertedCafeTable = returnedCafeTable;
    }

    @Test
    void createCafeTableWithExistingId() throws Exception {
        // Create the CafeTable with an existing ID
        cafeTable.setId("existing_id");
        CafeTableDTO cafeTableDTO = cafeTableMapper.toDto(cafeTable);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCafeTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cafeTableDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CafeTable in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkPkIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cafeTable.setPk(null);

        // Create the CafeTable, which fails.
        CafeTableDTO cafeTableDTO = cafeTableMapper.toDto(cafeTable);

        restCafeTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cafeTableDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cafeTable.setName(null);

        // Create the CafeTable, which fails.
        CafeTableDTO cafeTableDTO = cafeTableMapper.toDto(cafeTable);

        restCafeTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cafeTableDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkIndexIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cafeTable.setIndex(null);

        // Create the CafeTable, which fails.
        CafeTableDTO cafeTableDTO = cafeTableMapper.toDto(cafeTable);

        restCafeTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cafeTableDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cafeTable.setActive(null);

        // Create the CafeTable, which fails.
        CafeTableDTO cafeTableDTO = cafeTableMapper.toDto(cafeTable);

        restCafeTableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cafeTableDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllCafeTables() throws Exception {
        // Initialize the database
        insertedCafeTable = cafeTableRepository.save(cafeTable);

        // Get all the cafeTableList
        restCafeTableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cafeTable.getId())))
            .andExpect(jsonPath("$.[*].pk").value(hasItem(DEFAULT_PK.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    void getCafeTable() throws Exception {
        // Initialize the database
        insertedCafeTable = cafeTableRepository.save(cafeTable);

        // Get the cafeTable
        restCafeTableMockMvc
            .perform(get(ENTITY_API_URL_ID, cafeTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cafeTable.getId()))
            .andExpect(jsonPath("$.pk").value(DEFAULT_PK.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.index").value(DEFAULT_INDEX))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    void getNonExistingCafeTable() throws Exception {
        // Get the cafeTable
        restCafeTableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingCafeTable() throws Exception {
        // Initialize the database
        insertedCafeTable = cafeTableRepository.save(cafeTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cafeTable
        CafeTable updatedCafeTable = cafeTableRepository.findById(cafeTable.getId()).orElseThrow();
        updatedCafeTable.pk(UPDATED_PK).name(UPDATED_NAME).index(UPDATED_INDEX).active(UPDATED_ACTIVE);
        CafeTableDTO cafeTableDTO = cafeTableMapper.toDto(updatedCafeTable);

        restCafeTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cafeTableDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cafeTableDTO))
            )
            .andExpect(status().isOk());

        // Validate the CafeTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCafeTableToMatchAllProperties(updatedCafeTable);
    }

    @Test
    void putNonExistingCafeTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cafeTable.setId(UUID.randomUUID().toString());

        // Create the CafeTable
        CafeTableDTO cafeTableDTO = cafeTableMapper.toDto(cafeTable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCafeTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cafeTableDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cafeTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CafeTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchCafeTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cafeTable.setId(UUID.randomUUID().toString());

        // Create the CafeTable
        CafeTableDTO cafeTableDTO = cafeTableMapper.toDto(cafeTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCafeTableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cafeTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CafeTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamCafeTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cafeTable.setId(UUID.randomUUID().toString());

        // Create the CafeTable
        CafeTableDTO cafeTableDTO = cafeTableMapper.toDto(cafeTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCafeTableMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cafeTableDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CafeTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateCafeTableWithPatch() throws Exception {
        // Initialize the database
        insertedCafeTable = cafeTableRepository.save(cafeTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cafeTable using partial update
        CafeTable partialUpdatedCafeTable = new CafeTable();
        partialUpdatedCafeTable.setId(cafeTable.getId());

        partialUpdatedCafeTable.pk(UPDATED_PK).active(UPDATED_ACTIVE);

        restCafeTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCafeTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCafeTable))
            )
            .andExpect(status().isOk());

        // Validate the CafeTable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCafeTableUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCafeTable, cafeTable),
            getPersistedCafeTable(cafeTable)
        );
    }

    @Test
    void fullUpdateCafeTableWithPatch() throws Exception {
        // Initialize the database
        insertedCafeTable = cafeTableRepository.save(cafeTable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cafeTable using partial update
        CafeTable partialUpdatedCafeTable = new CafeTable();
        partialUpdatedCafeTable.setId(cafeTable.getId());

        partialUpdatedCafeTable.pk(UPDATED_PK).name(UPDATED_NAME).index(UPDATED_INDEX).active(UPDATED_ACTIVE);

        restCafeTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCafeTable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCafeTable))
            )
            .andExpect(status().isOk());

        // Validate the CafeTable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCafeTableUpdatableFieldsEquals(partialUpdatedCafeTable, getPersistedCafeTable(partialUpdatedCafeTable));
    }

    @Test
    void patchNonExistingCafeTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cafeTable.setId(UUID.randomUUID().toString());

        // Create the CafeTable
        CafeTableDTO cafeTableDTO = cafeTableMapper.toDto(cafeTable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCafeTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cafeTableDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cafeTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CafeTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchCafeTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cafeTable.setId(UUID.randomUUID().toString());

        // Create the CafeTable
        CafeTableDTO cafeTableDTO = cafeTableMapper.toDto(cafeTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCafeTableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cafeTableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CafeTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamCafeTable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cafeTable.setId(UUID.randomUUID().toString());

        // Create the CafeTable
        CafeTableDTO cafeTableDTO = cafeTableMapper.toDto(cafeTable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCafeTableMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cafeTableDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CafeTable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteCafeTable() throws Exception {
        // Initialize the database
        insertedCafeTable = cafeTableRepository.save(cafeTable);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cafeTable
        restCafeTableMockMvc
            .perform(delete(ENTITY_API_URL_ID, cafeTable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cafeTableRepository.count();
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

    protected CafeTable getPersistedCafeTable(CafeTable cafeTable) {
        return cafeTableRepository.findById(cafeTable.getId()).orElseThrow();
    }

    protected void assertPersistedCafeTableToMatchAllProperties(CafeTable expectedCafeTable) {
        assertCafeTableAllPropertiesEquals(expectedCafeTable, getPersistedCafeTable(expectedCafeTable));
    }

    protected void assertPersistedCafeTableToMatchUpdatableProperties(CafeTable expectedCafeTable) {
        assertCafeTableAllUpdatablePropertiesEquals(expectedCafeTable, getPersistedCafeTable(expectedCafeTable));
    }
}
