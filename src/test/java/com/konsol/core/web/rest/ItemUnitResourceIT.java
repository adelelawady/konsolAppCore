package com.konsol.core.web.rest;

import static com.konsol.core.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.konsol.core.IntegrationTest;
import com.konsol.core.domain.ItemUnit;
import com.konsol.core.repository.ItemUnitRepository;
import com.konsol.core.service.dto.ItemUnitDTO;
import com.konsol.core.service.mapper.ItemUnitMapper;
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
 * Integration tests for the {@link ItemUnitResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemUnitResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PIECES = new BigDecimal(0);
    private static final BigDecimal UPDATED_PIECES = new BigDecimal(1);

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    private static final String ENTITY_API_URL = "/api/item-units";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ItemUnitRepository itemUnitRepository;

    @Autowired
    private ItemUnitMapper itemUnitMapper;

    @Autowired
    private MockMvc restItemUnitMockMvc;

    private ItemUnit itemUnit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemUnit createEntity() {
        ItemUnit itemUnit = new ItemUnit().name(DEFAULT_NAME).pieces(DEFAULT_PIECES).price(DEFAULT_PRICE);
        return itemUnit;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemUnit createUpdatedEntity() {
        ItemUnit itemUnit = new ItemUnit().name(UPDATED_NAME).pieces(UPDATED_PIECES).price(UPDATED_PRICE);
        return itemUnit;
    }

    @BeforeEach
    public void initTest() {
        itemUnitRepository.deleteAll();
        itemUnit = createEntity();
    }

    @Test
    void createItemUnit() throws Exception {
        int databaseSizeBeforeCreate = itemUnitRepository.findAll().size();
        // Create the ItemUnit
        ItemUnitDTO itemUnitDTO = itemUnitMapper.toDto(itemUnit);
        restItemUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemUnitDTO)))
            .andExpect(status().isCreated());

        // Validate the ItemUnit in the database
        List<ItemUnit> itemUnitList = itemUnitRepository.findAll();
        assertThat(itemUnitList).hasSize(databaseSizeBeforeCreate + 1);
        ItemUnit testItemUnit = itemUnitList.get(itemUnitList.size() - 1);
        assertThat(testItemUnit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItemUnit.getPieces()).isEqualByComparingTo(DEFAULT_PIECES);
        assertThat(testItemUnit.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
    }

    @Test
    void createItemUnitWithExistingId() throws Exception {
        // Create the ItemUnit with an existing ID
        itemUnit.setId("existing_id");
        ItemUnitDTO itemUnitDTO = itemUnitMapper.toDto(itemUnit);

        int databaseSizeBeforeCreate = itemUnitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemUnitDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ItemUnit in the database
        List<ItemUnit> itemUnitList = itemUnitRepository.findAll();
        assertThat(itemUnitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemUnitRepository.findAll().size();
        // set the field null
        itemUnit.setName(null);

        // Create the ItemUnit, which fails.
        ItemUnitDTO itemUnitDTO = itemUnitMapper.toDto(itemUnit);

        restItemUnitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemUnitDTO)))
            .andExpect(status().isBadRequest());

        List<ItemUnit> itemUnitList = itemUnitRepository.findAll();
        assertThat(itemUnitList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllItemUnits() throws Exception {
        // Initialize the database
        itemUnit.setId(UUID.randomUUID().toString());
        itemUnitRepository.save(itemUnit);

        // Get all the itemUnitList
        restItemUnitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemUnit.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].pieces").value(hasItem(sameNumber(DEFAULT_PIECES))))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))));
    }

    @Test
    void getItemUnit() throws Exception {
        // Initialize the database
        itemUnit.setId(UUID.randomUUID().toString());
        itemUnitRepository.save(itemUnit);

        // Get the itemUnit
        restItemUnitMockMvc
            .perform(get(ENTITY_API_URL_ID, itemUnit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemUnit.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.pieces").value(sameNumber(DEFAULT_PIECES)))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)));
    }

    @Test
    void getNonExistingItemUnit() throws Exception {
        // Get the itemUnit
        restItemUnitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingItemUnit() throws Exception {
        // Initialize the database
        itemUnit.setId(UUID.randomUUID().toString());
        itemUnitRepository.save(itemUnit);

        int databaseSizeBeforeUpdate = itemUnitRepository.findAll().size();

        // Update the itemUnit
        ItemUnit updatedItemUnit = itemUnitRepository.findById(itemUnit.getId()).get();
        updatedItemUnit.name(UPDATED_NAME).pieces(UPDATED_PIECES).price(UPDATED_PRICE);
        ItemUnitDTO itemUnitDTO = itemUnitMapper.toDto(updatedItemUnit);

        restItemUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemUnitDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemUnitDTO))
            )
            .andExpect(status().isOk());

        // Validate the ItemUnit in the database
        List<ItemUnit> itemUnitList = itemUnitRepository.findAll();
        assertThat(itemUnitList).hasSize(databaseSizeBeforeUpdate);
        ItemUnit testItemUnit = itemUnitList.get(itemUnitList.size() - 1);
        assertThat(testItemUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemUnit.getPieces()).isEqualByComparingTo(UPDATED_PIECES);
        assertThat(testItemUnit.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    void putNonExistingItemUnit() throws Exception {
        int databaseSizeBeforeUpdate = itemUnitRepository.findAll().size();
        itemUnit.setId(UUID.randomUUID().toString());

        // Create the ItemUnit
        ItemUnitDTO itemUnitDTO = itemUnitMapper.toDto(itemUnit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemUnitDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemUnit in the database
        List<ItemUnit> itemUnitList = itemUnitRepository.findAll();
        assertThat(itemUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchItemUnit() throws Exception {
        int databaseSizeBeforeUpdate = itemUnitRepository.findAll().size();
        itemUnit.setId(UUID.randomUUID().toString());

        // Create the ItemUnit
        ItemUnitDTO itemUnitDTO = itemUnitMapper.toDto(itemUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemUnitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemUnit in the database
        List<ItemUnit> itemUnitList = itemUnitRepository.findAll();
        assertThat(itemUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamItemUnit() throws Exception {
        int databaseSizeBeforeUpdate = itemUnitRepository.findAll().size();
        itemUnit.setId(UUID.randomUUID().toString());

        // Create the ItemUnit
        ItemUnitDTO itemUnitDTO = itemUnitMapper.toDto(itemUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemUnitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemUnitDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemUnit in the database
        List<ItemUnit> itemUnitList = itemUnitRepository.findAll();
        assertThat(itemUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateItemUnitWithPatch() throws Exception {
        // Initialize the database
        itemUnit.setId(UUID.randomUUID().toString());
        itemUnitRepository.save(itemUnit);

        int databaseSizeBeforeUpdate = itemUnitRepository.findAll().size();

        // Update the itemUnit using partial update
        ItemUnit partialUpdatedItemUnit = new ItemUnit();
        partialUpdatedItemUnit.setId(itemUnit.getId());

        partialUpdatedItemUnit.pieces(UPDATED_PIECES).price(UPDATED_PRICE);

        restItemUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemUnit))
            )
            .andExpect(status().isOk());

        // Validate the ItemUnit in the database
        List<ItemUnit> itemUnitList = itemUnitRepository.findAll();
        assertThat(itemUnitList).hasSize(databaseSizeBeforeUpdate);
        ItemUnit testItemUnit = itemUnitList.get(itemUnitList.size() - 1);
        assertThat(testItemUnit.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItemUnit.getPieces()).isEqualByComparingTo(UPDATED_PIECES);
        assertThat(testItemUnit.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    void fullUpdateItemUnitWithPatch() throws Exception {
        // Initialize the database
        itemUnit.setId(UUID.randomUUID().toString());
        itemUnitRepository.save(itemUnit);

        int databaseSizeBeforeUpdate = itemUnitRepository.findAll().size();

        // Update the itemUnit using partial update
        ItemUnit partialUpdatedItemUnit = new ItemUnit();
        partialUpdatedItemUnit.setId(itemUnit.getId());

        partialUpdatedItemUnit.name(UPDATED_NAME).pieces(UPDATED_PIECES).price(UPDATED_PRICE);

        restItemUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemUnit.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemUnit))
            )
            .andExpect(status().isOk());

        // Validate the ItemUnit in the database
        List<ItemUnit> itemUnitList = itemUnitRepository.findAll();
        assertThat(itemUnitList).hasSize(databaseSizeBeforeUpdate);
        ItemUnit testItemUnit = itemUnitList.get(itemUnitList.size() - 1);
        assertThat(testItemUnit.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItemUnit.getPieces()).isEqualByComparingTo(UPDATED_PIECES);
        assertThat(testItemUnit.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
    }

    @Test
    void patchNonExistingItemUnit() throws Exception {
        int databaseSizeBeforeUpdate = itemUnitRepository.findAll().size();
        itemUnit.setId(UUID.randomUUID().toString());

        // Create the ItemUnit
        ItemUnitDTO itemUnitDTO = itemUnitMapper.toDto(itemUnit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemUnitDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemUnit in the database
        List<ItemUnit> itemUnitList = itemUnitRepository.findAll();
        assertThat(itemUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchItemUnit() throws Exception {
        int databaseSizeBeforeUpdate = itemUnitRepository.findAll().size();
        itemUnit.setId(UUID.randomUUID().toString());

        // Create the ItemUnit
        ItemUnitDTO itemUnitDTO = itemUnitMapper.toDto(itemUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemUnitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemUnitDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemUnit in the database
        List<ItemUnit> itemUnitList = itemUnitRepository.findAll();
        assertThat(itemUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamItemUnit() throws Exception {
        int databaseSizeBeforeUpdate = itemUnitRepository.findAll().size();
        itemUnit.setId(UUID.randomUUID().toString());

        // Create the ItemUnit
        ItemUnitDTO itemUnitDTO = itemUnitMapper.toDto(itemUnit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemUnitMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(itemUnitDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemUnit in the database
        List<ItemUnit> itemUnitList = itemUnitRepository.findAll();
        assertThat(itemUnitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteItemUnit() throws Exception {
        // Initialize the database
        itemUnit.setId(UUID.randomUUID().toString());
        itemUnitRepository.save(itemUnit);

        int databaseSizeBeforeDelete = itemUnitRepository.findAll().size();

        // Delete the itemUnit
        restItemUnitMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemUnit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemUnit> itemUnitList = itemUnitRepository.findAll();
        assertThat(itemUnitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
