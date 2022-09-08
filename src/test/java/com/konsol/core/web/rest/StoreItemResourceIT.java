package com.konsol.core.web.rest;

import static com.konsol.core.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.konsol.core.IntegrationTest;
import com.konsol.core.domain.StoreItem;
import com.konsol.core.repository.StoreItemRepository;
import com.konsol.core.service.dto.StoreItemDTO;
import com.konsol.core.service.mapper.StoreItemMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link StoreItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StoreItemResourceIT {

    private static final BigDecimal DEFAULT_QTY = new BigDecimal(0);
    private static final BigDecimal UPDATED_QTY = new BigDecimal(1);

    private static final String ENTITY_API_URL = "/api/store-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private StoreItemRepository storeItemRepository;

    @Autowired
    private StoreItemMapper storeItemMapper;

    @Autowired
    private MockMvc restStoreItemMockMvc;

    private StoreItem storeItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StoreItem createEntity() {
        StoreItem storeItem = new StoreItem().qty(DEFAULT_QTY);
        return storeItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StoreItem createUpdatedEntity() {
        StoreItem storeItem = new StoreItem().qty(UPDATED_QTY);
        return storeItem;
    }

    @BeforeEach
    public void initTest() {
        storeItemRepository.deleteAll();
        storeItem = createEntity();
    }

    @Test
    void createStoreItem() throws Exception {
        int databaseSizeBeforeCreate = storeItemRepository.findAll().size();
        // Create the StoreItem
        StoreItemDTO storeItemDTO = storeItemMapper.toDto(storeItem);
        restStoreItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(storeItemDTO)))
            .andExpect(status().isCreated());

        // Validate the StoreItem in the database
        List<StoreItem> storeItemList = storeItemRepository.findAll();
        assertThat(storeItemList).hasSize(databaseSizeBeforeCreate + 1);
        StoreItem testStoreItem = storeItemList.get(storeItemList.size() - 1);
        assertThat(testStoreItem.getQty()).isEqualByComparingTo(DEFAULT_QTY);
    }

    @Test
    void createStoreItemWithExistingId() throws Exception {
        // Create the StoreItem with an existing ID
        storeItem.setId("existing_id");
        StoreItemDTO storeItemDTO = storeItemMapper.toDto(storeItem);

        int databaseSizeBeforeCreate = storeItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStoreItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(storeItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StoreItem in the database
        List<StoreItem> storeItemList = storeItemRepository.findAll();
        assertThat(storeItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllStoreItems() throws Exception {
        // Initialize the database
        storeItem.setId(UUID.randomUUID().toString());
        storeItemRepository.save(storeItem);

        // Get all the storeItemList
        restStoreItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(storeItem.getId())))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(sameNumber(DEFAULT_QTY))));
    }

    @Test
    void getStoreItem() throws Exception {
        // Initialize the database
        storeItem.setId(UUID.randomUUID().toString());
        storeItemRepository.save(storeItem);

        // Get the storeItem
        restStoreItemMockMvc
            .perform(get(ENTITY_API_URL_ID, storeItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(storeItem.getId()))
            .andExpect(jsonPath("$.qty").value(sameNumber(DEFAULT_QTY)));
    }

    @Test
    void getNonExistingStoreItem() throws Exception {
        // Get the storeItem
        restStoreItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingStoreItem() throws Exception {
        // Initialize the database
        storeItem.setId(UUID.randomUUID().toString());
        storeItemRepository.save(storeItem);

        int databaseSizeBeforeUpdate = storeItemRepository.findAll().size();

        // Update the storeItem
        StoreItem updatedStoreItem = storeItemRepository.findById(storeItem.getId()).get();
        updatedStoreItem.qty(UPDATED_QTY);
        StoreItemDTO storeItemDTO = storeItemMapper.toDto(updatedStoreItem);

        restStoreItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storeItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storeItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the StoreItem in the database
        List<StoreItem> storeItemList = storeItemRepository.findAll();
        assertThat(storeItemList).hasSize(databaseSizeBeforeUpdate);
        StoreItem testStoreItem = storeItemList.get(storeItemList.size() - 1);
        assertThat(testStoreItem.getQty()).isEqualByComparingTo(UPDATED_QTY);
    }

    @Test
    void putNonExistingStoreItem() throws Exception {
        int databaseSizeBeforeUpdate = storeItemRepository.findAll().size();
        storeItem.setId(UUID.randomUUID().toString());

        // Create the StoreItem
        StoreItemDTO storeItemDTO = storeItemMapper.toDto(storeItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoreItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, storeItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storeItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StoreItem in the database
        List<StoreItem> storeItemList = storeItemRepository.findAll();
        assertThat(storeItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchStoreItem() throws Exception {
        int databaseSizeBeforeUpdate = storeItemRepository.findAll().size();
        storeItem.setId(UUID.randomUUID().toString());

        // Create the StoreItem
        StoreItemDTO storeItemDTO = storeItemMapper.toDto(storeItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoreItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(storeItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StoreItem in the database
        List<StoreItem> storeItemList = storeItemRepository.findAll();
        assertThat(storeItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamStoreItem() throws Exception {
        int databaseSizeBeforeUpdate = storeItemRepository.findAll().size();
        storeItem.setId(UUID.randomUUID().toString());

        // Create the StoreItem
        StoreItemDTO storeItemDTO = storeItemMapper.toDto(storeItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoreItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(storeItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StoreItem in the database
        List<StoreItem> storeItemList = storeItemRepository.findAll();
        assertThat(storeItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateStoreItemWithPatch() throws Exception {
        // Initialize the database
        storeItem.setId(UUID.randomUUID().toString());
        storeItemRepository.save(storeItem);

        int databaseSizeBeforeUpdate = storeItemRepository.findAll().size();

        // Update the storeItem using partial update
        StoreItem partialUpdatedStoreItem = new StoreItem();
        partialUpdatedStoreItem.setId(storeItem.getId());

        partialUpdatedStoreItem.qty(UPDATED_QTY);

        restStoreItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStoreItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStoreItem))
            )
            .andExpect(status().isOk());

        // Validate the StoreItem in the database
        List<StoreItem> storeItemList = storeItemRepository.findAll();
        assertThat(storeItemList).hasSize(databaseSizeBeforeUpdate);
        StoreItem testStoreItem = storeItemList.get(storeItemList.size() - 1);
        assertThat(testStoreItem.getQty()).isEqualByComparingTo(UPDATED_QTY);
    }

    @Test
    void fullUpdateStoreItemWithPatch() throws Exception {
        // Initialize the database
        storeItem.setId(UUID.randomUUID().toString());
        storeItemRepository.save(storeItem);

        int databaseSizeBeforeUpdate = storeItemRepository.findAll().size();

        // Update the storeItem using partial update
        StoreItem partialUpdatedStoreItem = new StoreItem();
        partialUpdatedStoreItem.setId(storeItem.getId());

        partialUpdatedStoreItem.qty(UPDATED_QTY);

        restStoreItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStoreItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStoreItem))
            )
            .andExpect(status().isOk());

        // Validate the StoreItem in the database
        List<StoreItem> storeItemList = storeItemRepository.findAll();
        assertThat(storeItemList).hasSize(databaseSizeBeforeUpdate);
        StoreItem testStoreItem = storeItemList.get(storeItemList.size() - 1);
        assertThat(testStoreItem.getQty()).isEqualByComparingTo(UPDATED_QTY);
    }

    @Test
    void patchNonExistingStoreItem() throws Exception {
        int databaseSizeBeforeUpdate = storeItemRepository.findAll().size();
        storeItem.setId(UUID.randomUUID().toString());

        // Create the StoreItem
        StoreItemDTO storeItemDTO = storeItemMapper.toDto(storeItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStoreItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, storeItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storeItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StoreItem in the database
        List<StoreItem> storeItemList = storeItemRepository.findAll();
        assertThat(storeItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchStoreItem() throws Exception {
        int databaseSizeBeforeUpdate = storeItemRepository.findAll().size();
        storeItem.setId(UUID.randomUUID().toString());

        // Create the StoreItem
        StoreItemDTO storeItemDTO = storeItemMapper.toDto(storeItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoreItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(storeItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the StoreItem in the database
        List<StoreItem> storeItemList = storeItemRepository.findAll();
        assertThat(storeItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamStoreItem() throws Exception {
        int databaseSizeBeforeUpdate = storeItemRepository.findAll().size();
        storeItem.setId(UUID.randomUUID().toString());

        // Create the StoreItem
        StoreItemDTO storeItemDTO = storeItemMapper.toDto(storeItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStoreItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(storeItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StoreItem in the database
        List<StoreItem> storeItemList = storeItemRepository.findAll();
        assertThat(storeItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteStoreItem() throws Exception {
        // Initialize the database
        storeItem.setId(UUID.randomUUID().toString());
        storeItemRepository.save(storeItem);

        int databaseSizeBeforeDelete = storeItemRepository.findAll().size();

        // Delete the storeItem
        restStoreItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, storeItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StoreItem> storeItemList = storeItemRepository.findAll();
        assertThat(storeItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
