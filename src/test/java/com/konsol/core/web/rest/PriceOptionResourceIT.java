package com.konsol.core.web.rest;

import static com.konsol.core.domain.PriceOptionAsserts.*;
import static com.konsol.core.web.rest.TestUtil.createUpdateProxyForBean;
import static com.konsol.core.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.konsol.core.IntegrationTest;
import com.konsol.core.domain.PriceOption;
import com.konsol.core.repository.PriceOptionRepository;
import com.konsol.core.service.dto.PriceOptionDTO;
import com.konsol.core.service.mapper.PriceOptionMapper;
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
 * Integration tests for the {@link PriceOptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PriceOptionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(2);

    private static final String DEFAULT_PRODUCT_ID = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/price-options";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PriceOptionRepository priceOptionRepository;

    @Autowired
    private PriceOptionMapper priceOptionMapper;

    @Autowired
    private MockMvc restPriceOptionMockMvc;

    private PriceOption priceOption;

    private PriceOption insertedPriceOption;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PriceOption createEntity() {
        return new PriceOption().name(DEFAULT_NAME).value(DEFAULT_VALUE).productId(DEFAULT_PRODUCT_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PriceOption createUpdatedEntity() {
        return new PriceOption().name(UPDATED_NAME).value(UPDATED_VALUE).productId(UPDATED_PRODUCT_ID);
    }

    @BeforeEach
    public void initTest() {
        priceOption = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedPriceOption != null) {
            priceOptionRepository.delete(insertedPriceOption);
            insertedPriceOption = null;
        }
    }

    @Test
    void createPriceOption() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PriceOption
        PriceOptionDTO priceOptionDTO = priceOptionMapper.toDto(priceOption);
        var returnedPriceOptionDTO = om.readValue(
            restPriceOptionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(priceOptionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PriceOptionDTO.class
        );

        // Validate the PriceOption in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPriceOption = priceOptionMapper.toEntity(returnedPriceOptionDTO);
        assertPriceOptionUpdatableFieldsEquals(returnedPriceOption, getPersistedPriceOption(returnedPriceOption));

        insertedPriceOption = returnedPriceOption;
    }

    @Test
    void createPriceOptionWithExistingId() throws Exception {
        // Create the PriceOption with an existing ID
        priceOption.setId("existing_id");
        PriceOptionDTO priceOptionDTO = priceOptionMapper.toDto(priceOption);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPriceOptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(priceOptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PriceOption in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        priceOption.setName(null);

        // Create the PriceOption, which fails.
        PriceOptionDTO priceOptionDTO = priceOptionMapper.toDto(priceOption);

        restPriceOptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(priceOptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        priceOption.setValue(null);

        // Create the PriceOption, which fails.
        PriceOptionDTO priceOptionDTO = priceOptionMapper.toDto(priceOption);

        restPriceOptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(priceOptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void checkProductIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        priceOption.setProductId(null);

        // Create the PriceOption, which fails.
        PriceOptionDTO priceOptionDTO = priceOptionMapper.toDto(priceOption);

        restPriceOptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(priceOptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    void getAllPriceOptions() throws Exception {
        // Initialize the database
        insertedPriceOption = priceOptionRepository.save(priceOption);

        // Get all the priceOptionList
        restPriceOptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(priceOption.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(sameNumber(DEFAULT_VALUE))))
            .andExpect(jsonPath("$.[*].productId").value(hasItem(DEFAULT_PRODUCT_ID)));
    }

    @Test
    void getPriceOption() throws Exception {
        // Initialize the database
        insertedPriceOption = priceOptionRepository.save(priceOption);

        // Get the priceOption
        restPriceOptionMockMvc
            .perform(get(ENTITY_API_URL_ID, priceOption.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(priceOption.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(sameNumber(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.productId").value(DEFAULT_PRODUCT_ID));
    }

    @Test
    void getNonExistingPriceOption() throws Exception {
        // Get the priceOption
        restPriceOptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingPriceOption() throws Exception {
        // Initialize the database
        insertedPriceOption = priceOptionRepository.save(priceOption);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the priceOption
        PriceOption updatedPriceOption = priceOptionRepository.findById(priceOption.getId()).orElseThrow();
        updatedPriceOption.name(UPDATED_NAME).value(UPDATED_VALUE).productId(UPDATED_PRODUCT_ID);
        PriceOptionDTO priceOptionDTO = priceOptionMapper.toDto(updatedPriceOption);

        restPriceOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, priceOptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(priceOptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the PriceOption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPriceOptionToMatchAllProperties(updatedPriceOption);
    }

    @Test
    void putNonExistingPriceOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        priceOption.setId(UUID.randomUUID().toString());

        // Create the PriceOption
        PriceOptionDTO priceOptionDTO = priceOptionMapper.toDto(priceOption);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriceOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, priceOptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(priceOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceOption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPriceOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        priceOption.setId(UUID.randomUUID().toString());

        // Create the PriceOption
        PriceOptionDTO priceOptionDTO = priceOptionMapper.toDto(priceOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceOptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(priceOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceOption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPriceOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        priceOption.setId(UUID.randomUUID().toString());

        // Create the PriceOption
        PriceOptionDTO priceOptionDTO = priceOptionMapper.toDto(priceOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceOptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(priceOptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PriceOption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePriceOptionWithPatch() throws Exception {
        // Initialize the database
        insertedPriceOption = priceOptionRepository.save(priceOption);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the priceOption using partial update
        PriceOption partialUpdatedPriceOption = new PriceOption();
        partialUpdatedPriceOption.setId(priceOption.getId());

        restPriceOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPriceOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPriceOption))
            )
            .andExpect(status().isOk());

        // Validate the PriceOption in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPriceOptionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPriceOption, priceOption),
            getPersistedPriceOption(priceOption)
        );
    }

    @Test
    void fullUpdatePriceOptionWithPatch() throws Exception {
        // Initialize the database
        insertedPriceOption = priceOptionRepository.save(priceOption);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the priceOption using partial update
        PriceOption partialUpdatedPriceOption = new PriceOption();
        partialUpdatedPriceOption.setId(priceOption.getId());

        partialUpdatedPriceOption.name(UPDATED_NAME).value(UPDATED_VALUE).productId(UPDATED_PRODUCT_ID);

        restPriceOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPriceOption.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPriceOption))
            )
            .andExpect(status().isOk());

        // Validate the PriceOption in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPriceOptionUpdatableFieldsEquals(partialUpdatedPriceOption, getPersistedPriceOption(partialUpdatedPriceOption));
    }

    @Test
    void patchNonExistingPriceOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        priceOption.setId(UUID.randomUUID().toString());

        // Create the PriceOption
        PriceOptionDTO priceOptionDTO = priceOptionMapper.toDto(priceOption);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriceOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, priceOptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(priceOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceOption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPriceOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        priceOption.setId(UUID.randomUUID().toString());

        // Create the PriceOption
        PriceOptionDTO priceOptionDTO = priceOptionMapper.toDto(priceOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceOptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(priceOptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceOption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPriceOption() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        priceOption.setId(UUID.randomUUID().toString());

        // Create the PriceOption
        PriceOptionDTO priceOptionDTO = priceOptionMapper.toDto(priceOption);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceOptionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(priceOptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PriceOption in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePriceOption() throws Exception {
        // Initialize the database
        insertedPriceOption = priceOptionRepository.save(priceOption);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the priceOption
        restPriceOptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, priceOption.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return priceOptionRepository.count();
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

    protected PriceOption getPersistedPriceOption(PriceOption priceOption) {
        return priceOptionRepository.findById(priceOption.getId()).orElseThrow();
    }

    protected void assertPersistedPriceOptionToMatchAllProperties(PriceOption expectedPriceOption) {
        assertPriceOptionAllPropertiesEquals(expectedPriceOption, getPersistedPriceOption(expectedPriceOption));
    }

    protected void assertPersistedPriceOptionToMatchUpdatableProperties(PriceOption expectedPriceOption) {
        assertPriceOptionAllUpdatablePropertiesEquals(expectedPriceOption, getPersistedPriceOption(expectedPriceOption));
    }
}
