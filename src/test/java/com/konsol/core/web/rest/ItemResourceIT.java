package com.konsol.core.web.rest;

import static com.konsol.core.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.konsol.core.IntegrationTest;
import com.konsol.core.domain.Item;
import com.konsol.core.repository.ItemRepository;
import com.konsol.core.service.ItemService;
import com.konsol.core.service.dto.ItemDTO;
import com.konsol.core.service.mapper.ItemMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for the {@link ItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ItemResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_BARCODE = "AAAAAAAAAA";
    private static final String UPDATED_BARCODE = "BBBBBBBBBB";

    private static final String DEFAULT_PRICE_1 = "AAAAAAAAAA";
    private static final String UPDATED_PRICE_1 = "BBBBBBBBBB";

    private static final String DEFAULT_PRICE_2 = "AAAAAAAAAA";
    private static final String UPDATED_PRICE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_PRICE_3 = "AAAAAAAAAA";
    private static final String UPDATED_PRICE_3 = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_QTY = new BigDecimal(0);
    private static final BigDecimal UPDATED_QTY = new BigDecimal(1);

    private static final BigDecimal DEFAULT_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_COST = new BigDecimal(1);

    private static final Integer DEFAULT_INDEX = 1;
    private static final Integer UPDATED_INDEX = 2;

    private static final String ENTITY_API_URL = "/api/items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ItemRepository itemRepository;

    @Mock
    private ItemRepository itemRepositoryMock;

    @Autowired
    private ItemMapper itemMapper;

    @Mock
    private ItemService itemServiceMock;

    @Autowired
    private MockMvc restItemMockMvc;

    private Item item;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createEntity() {
        Item item = new Item()
            .name(DEFAULT_NAME)
            .barcode(DEFAULT_BARCODE)
            .price1(DEFAULT_PRICE_1)
            .price2(DEFAULT_PRICE_2)
            .price3(DEFAULT_PRICE_3)
            .category(DEFAULT_CATEGORY)
            .qty(DEFAULT_QTY)
            .cost(DEFAULT_COST)
            .index(DEFAULT_INDEX);
        return item;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Item createUpdatedEntity() {
        Item item = new Item()
            .name(UPDATED_NAME)
            .barcode(UPDATED_BARCODE)
            .price1(UPDATED_PRICE_1)
            .price2(UPDATED_PRICE_2)
            .price3(UPDATED_PRICE_3)
            .category(UPDATED_CATEGORY)
            .qty(UPDATED_QTY)
            .cost(UPDATED_COST)
            .index(UPDATED_INDEX);
        return item;
    }

    @BeforeEach
    public void initTest() {
        itemRepository.deleteAll();
        item = createEntity();
    }

    @Test
    void createItem() throws Exception {
        int databaseSizeBeforeCreate = itemRepository.findAll().size();
        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);
        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isCreated());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeCreate + 1);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItem.getBarcode()).isEqualTo(DEFAULT_BARCODE);
        assertThat(testItem.getPrice1()).isEqualTo(DEFAULT_PRICE_1);
        assertThat(testItem.getPrice2()).isEqualTo(DEFAULT_PRICE_2);
        assertThat(testItem.getPrice3()).isEqualTo(DEFAULT_PRICE_3);
        assertThat(testItem.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testItem.getQty()).isEqualByComparingTo(DEFAULT_QTY);
        assertThat(testItem.getCost()).isEqualByComparingTo(DEFAULT_COST);
        assertThat(testItem.getIndex()).isEqualTo(DEFAULT_INDEX);
    }

    @Test
    void createItemWithExistingId() throws Exception {
        // Create the Item with an existing ID
        item.setId("existing_id");
        ItemDTO itemDTO = itemMapper.toDto(item);

        int databaseSizeBeforeCreate = itemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemRepository.findAll().size();
        // set the field null
        item.setName(null);

        // Create the Item, which fails.
        ItemDTO itemDTO = itemMapper.toDto(item);

        restItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isBadRequest());

        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllItems() throws Exception {
        // Initialize the database
        item.setId(UUID.randomUUID().toString());
        itemRepository.save(item);

        // Get all the itemList
        restItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].barcode").value(hasItem(DEFAULT_BARCODE)))
            .andExpect(jsonPath("$.[*].price1").value(hasItem(DEFAULT_PRICE_1)))
            .andExpect(jsonPath("$.[*].price2").value(hasItem(DEFAULT_PRICE_2)))
            .andExpect(jsonPath("$.[*].price3").value(hasItem(DEFAULT_PRICE_3)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].qty").value(hasItem(sameNumber(DEFAULT_QTY))))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(sameNumber(DEFAULT_COST))))
            .andExpect(jsonPath("$.[*].index").value(hasItem(DEFAULT_INDEX)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(itemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(itemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(itemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(itemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getItem() throws Exception {
        // Initialize the database
        item.setId(UUID.randomUUID().toString());
        itemRepository.save(item);

        // Get the item
        restItemMockMvc
            .perform(get(ENTITY_API_URL_ID, item.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(item.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.barcode").value(DEFAULT_BARCODE))
            .andExpect(jsonPath("$.price1").value(DEFAULT_PRICE_1))
            .andExpect(jsonPath("$.price2").value(DEFAULT_PRICE_2))
            .andExpect(jsonPath("$.price3").value(DEFAULT_PRICE_3))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.qty").value(sameNumber(DEFAULT_QTY)))
            .andExpect(jsonPath("$.cost").value(sameNumber(DEFAULT_COST)))
            .andExpect(jsonPath("$.index").value(DEFAULT_INDEX));
    }

    @Test
    void getNonExistingItem() throws Exception {
        // Get the item
        restItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingItem() throws Exception {
        // Initialize the database
        item.setId(UUID.randomUUID().toString());
        itemRepository.save(item);

        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item
        Item updatedItem = itemRepository.findById(item.getId()).get();
        updatedItem
            .name(UPDATED_NAME)
            .barcode(UPDATED_BARCODE)
            .price1(UPDATED_PRICE_1)
            .price2(UPDATED_PRICE_2)
            .price3(UPDATED_PRICE_3)
            .category(UPDATED_CATEGORY)
            .qty(UPDATED_QTY)
            .cost(UPDATED_COST)
            .index(UPDATED_INDEX);
        ItemDTO itemDTO = itemMapper.toDto(updatedItem);

        restItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemDTO))
            )
            .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItem.getBarcode()).isEqualTo(UPDATED_BARCODE);
        assertThat(testItem.getPrice1()).isEqualTo(UPDATED_PRICE_1);
        assertThat(testItem.getPrice2()).isEqualTo(UPDATED_PRICE_2);
        assertThat(testItem.getPrice3()).isEqualTo(UPDATED_PRICE_3);
        assertThat(testItem.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testItem.getQty()).isEqualByComparingTo(UPDATED_QTY);
        assertThat(testItem.getCost()).isEqualByComparingTo(UPDATED_COST);
        assertThat(testItem.getIndex()).isEqualTo(UPDATED_INDEX);
    }

    @Test
    void putNonExistingItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(UUID.randomUUID().toString());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(UUID.randomUUID().toString());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(UUID.randomUUID().toString());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateItemWithPatch() throws Exception {
        // Initialize the database
        item.setId(UUID.randomUUID().toString());
        itemRepository.save(item);

        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item using partial update
        Item partialUpdatedItem = new Item();
        partialUpdatedItem.setId(item.getId());

        partialUpdatedItem.price2(UPDATED_PRICE_2).cost(UPDATED_COST).index(UPDATED_INDEX);

        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItem))
            )
            .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testItem.getBarcode()).isEqualTo(DEFAULT_BARCODE);
        assertThat(testItem.getPrice1()).isEqualTo(DEFAULT_PRICE_1);
        assertThat(testItem.getPrice2()).isEqualTo(UPDATED_PRICE_2);
        assertThat(testItem.getPrice3()).isEqualTo(DEFAULT_PRICE_3);
        assertThat(testItem.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testItem.getQty()).isEqualByComparingTo(DEFAULT_QTY);
        assertThat(testItem.getCost()).isEqualByComparingTo(UPDATED_COST);
        assertThat(testItem.getIndex()).isEqualTo(UPDATED_INDEX);
    }

    @Test
    void fullUpdateItemWithPatch() throws Exception {
        // Initialize the database
        item.setId(UUID.randomUUID().toString());
        itemRepository.save(item);

        int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item using partial update
        Item partialUpdatedItem = new Item();
        partialUpdatedItem.setId(item.getId());

        partialUpdatedItem
            .name(UPDATED_NAME)
            .barcode(UPDATED_BARCODE)
            .price1(UPDATED_PRICE_1)
            .price2(UPDATED_PRICE_2)
            .price3(UPDATED_PRICE_3)
            .category(UPDATED_CATEGORY)
            .qty(UPDATED_QTY)
            .cost(UPDATED_COST)
            .index(UPDATED_INDEX);

        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItem))
            )
            .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
        Item testItem = itemList.get(itemList.size() - 1);
        assertThat(testItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testItem.getBarcode()).isEqualTo(UPDATED_BARCODE);
        assertThat(testItem.getPrice1()).isEqualTo(UPDATED_PRICE_1);
        assertThat(testItem.getPrice2()).isEqualTo(UPDATED_PRICE_2);
        assertThat(testItem.getPrice3()).isEqualTo(UPDATED_PRICE_3);
        assertThat(testItem.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testItem.getQty()).isEqualByComparingTo(UPDATED_QTY);
        assertThat(testItem.getCost()).isEqualByComparingTo(UPDATED_COST);
        assertThat(testItem.getIndex()).isEqualTo(UPDATED_INDEX);
    }

    @Test
    void patchNonExistingItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(UUID.randomUUID().toString());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(UUID.randomUUID().toString());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamItem() throws Exception {
        int databaseSizeBeforeUpdate = itemRepository.findAll().size();
        item.setId(UUID.randomUUID().toString());

        // Create the Item
        ItemDTO itemDTO = itemMapper.toDto(item);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(itemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Item in the database
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteItem() throws Exception {
        // Initialize the database
        item.setId(UUID.randomUUID().toString());
        itemRepository.save(item);

        int databaseSizeBeforeDelete = itemRepository.findAll().size();

        // Delete the item
        restItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, item.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Item> itemList = itemRepository.findAll();
        assertThat(itemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
