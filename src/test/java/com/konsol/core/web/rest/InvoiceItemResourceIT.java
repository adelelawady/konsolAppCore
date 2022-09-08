package com.konsol.core.web.rest;

import static com.konsol.core.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.konsol.core.IntegrationTest;
import com.konsol.core.domain.InvoiceItem;
import com.konsol.core.repository.InvoiceItemRepository;
import com.konsol.core.service.dto.InvoiceItemDTO;
import com.konsol.core.service.mapper.InvoiceItemMapper;
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
 * Integration tests for the {@link InvoiceItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvoiceItemResourceIT {

    private static final String DEFAULT_PK = "AAAAAAAAAA";
    private static final String UPDATED_PK = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_UNIT_PIECES = new BigDecimal(0);
    private static final BigDecimal UPDATED_UNIT_PIECES = new BigDecimal(1);

    private static final BigDecimal DEFAULT_USER_QTY = new BigDecimal(0);
    private static final BigDecimal UPDATED_USER_QTY = new BigDecimal(1);

    private static final BigDecimal DEFAULT_UNIT_QTY_IN = new BigDecimal(0);
    private static final BigDecimal UPDATED_UNIT_QTY_IN = new BigDecimal(1);

    private static final BigDecimal DEFAULT_UNIT_QTY_OUT = new BigDecimal(0);
    private static final BigDecimal UPDATED_UNIT_QTY_OUT = new BigDecimal(1);

    private static final BigDecimal DEFAULT_UNIT_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_UNIT_COST = new BigDecimal(1);

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal(1);

    private static final Integer DEFAULT_DISCOUNT_PER = 0;
    private static final Integer UPDATED_DISCOUNT_PER = 1;

    private static final BigDecimal DEFAULT_DISCOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_DISCOUNT = new BigDecimal(1);

    private static final BigDecimal DEFAULT_TOTAL_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_COST = new BigDecimal(1);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_QTY_IN = new BigDecimal(0);
    private static final BigDecimal UPDATED_QTY_IN = new BigDecimal(1);

    private static final BigDecimal DEFAULT_QTY_OUT = new BigDecimal(0);
    private static final BigDecimal UPDATED_QTY_OUT = new BigDecimal(1);

    private static final BigDecimal DEFAULT_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_COST = new BigDecimal(1);

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_NET_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_NET_COST = new BigDecimal(1);

    private static final BigDecimal DEFAULT_NET_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_NET_PRICE = new BigDecimal(1);

    private static final String ENTITY_API_URL = "/api/invoice-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private InvoiceItemRepository invoiceItemRepository;

    @Autowired
    private InvoiceItemMapper invoiceItemMapper;

    @Autowired
    private MockMvc restInvoiceItemMockMvc;

    private InvoiceItem invoiceItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceItem createEntity() {
        InvoiceItem invoiceItem = new InvoiceItem()
            .pk(DEFAULT_PK)
            .unit(DEFAULT_UNIT)
            .unitPieces(DEFAULT_UNIT_PIECES)
            .userQty(DEFAULT_USER_QTY)
            .unitQtyIn(DEFAULT_UNIT_QTY_IN)
            .unitQtyOut(DEFAULT_UNIT_QTY_OUT)
            .unitCost(DEFAULT_UNIT_COST)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .discountPer(DEFAULT_DISCOUNT_PER)
            .discount(DEFAULT_DISCOUNT)
            .totalCost(DEFAULT_TOTAL_COST)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .qtyIn(DEFAULT_QTY_IN)
            .qtyOut(DEFAULT_QTY_OUT)
            .cost(DEFAULT_COST)
            .price(DEFAULT_PRICE)
            .netCost(DEFAULT_NET_COST)
            .netPrice(DEFAULT_NET_PRICE);
        return invoiceItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvoiceItem createUpdatedEntity() {
        InvoiceItem invoiceItem = new InvoiceItem()
            .pk(UPDATED_PK)
            .unit(UPDATED_UNIT)
            .unitPieces(UPDATED_UNIT_PIECES)
            .userQty(UPDATED_USER_QTY)
            .unitQtyIn(UPDATED_UNIT_QTY_IN)
            .unitQtyOut(UPDATED_UNIT_QTY_OUT)
            .unitCost(UPDATED_UNIT_COST)
            .unitPrice(UPDATED_UNIT_PRICE)
            .discountPer(UPDATED_DISCOUNT_PER)
            .discount(UPDATED_DISCOUNT)
            .totalCost(UPDATED_TOTAL_COST)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .qtyIn(UPDATED_QTY_IN)
            .qtyOut(UPDATED_QTY_OUT)
            .cost(UPDATED_COST)
            .price(UPDATED_PRICE)
            .netCost(UPDATED_NET_COST)
            .netPrice(UPDATED_NET_PRICE);
        return invoiceItem;
    }

    @BeforeEach
    public void initTest() {
        invoiceItemRepository.deleteAll();
        invoiceItem = createEntity();
    }

    @Test
    void createInvoiceItem() throws Exception {
        int databaseSizeBeforeCreate = invoiceItemRepository.findAll().size();
        // Create the InvoiceItem
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);
        restInvoiceItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO))
            )
            .andExpect(status().isCreated());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeCreate + 1);
        InvoiceItem testInvoiceItem = invoiceItemList.get(invoiceItemList.size() - 1);
        assertThat(testInvoiceItem.getPk()).isEqualTo(DEFAULT_PK);
        assertThat(testInvoiceItem.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testInvoiceItem.getUnitPieces()).isEqualByComparingTo(DEFAULT_UNIT_PIECES);
        assertThat(testInvoiceItem.getUserQty()).isEqualByComparingTo(DEFAULT_USER_QTY);
        assertThat(testInvoiceItem.getUnitQtyIn()).isEqualByComparingTo(DEFAULT_UNIT_QTY_IN);
        assertThat(testInvoiceItem.getUnitQtyOut()).isEqualByComparingTo(DEFAULT_UNIT_QTY_OUT);
        assertThat(testInvoiceItem.getUnitCost()).isEqualByComparingTo(DEFAULT_UNIT_COST);
        assertThat(testInvoiceItem.getUnitPrice()).isEqualByComparingTo(DEFAULT_UNIT_PRICE);
        assertThat(testInvoiceItem.getDiscountPer()).isEqualTo(DEFAULT_DISCOUNT_PER);
        assertThat(testInvoiceItem.getDiscount()).isEqualByComparingTo(DEFAULT_DISCOUNT);
        assertThat(testInvoiceItem.getTotalCost()).isEqualByComparingTo(DEFAULT_TOTAL_COST);
        assertThat(testInvoiceItem.getTotalPrice()).isEqualByComparingTo(DEFAULT_TOTAL_PRICE);
        assertThat(testInvoiceItem.getQtyIn()).isEqualByComparingTo(DEFAULT_QTY_IN);
        assertThat(testInvoiceItem.getQtyOut()).isEqualByComparingTo(DEFAULT_QTY_OUT);
        assertThat(testInvoiceItem.getCost()).isEqualByComparingTo(DEFAULT_COST);
        assertThat(testInvoiceItem.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testInvoiceItem.getNetCost()).isEqualByComparingTo(DEFAULT_NET_COST);
        assertThat(testInvoiceItem.getNetPrice()).isEqualByComparingTo(DEFAULT_NET_PRICE);
    }

    @Test
    void createInvoiceItemWithExistingId() throws Exception {
        // Create the InvoiceItem with an existing ID
        invoiceItem.setId("existing_id");
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        int databaseSizeBeforeCreate = invoiceItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceItemMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllInvoiceItems() throws Exception {
        // Initialize the database
        invoiceItem.setId(UUID.randomUUID().toString());
        invoiceItemRepository.save(invoiceItem);

        // Get all the invoiceItemList
        restInvoiceItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoiceItem.getId())))
            .andExpect(jsonPath("$.[*].pk").value(hasItem(DEFAULT_PK)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].unitPieces").value(hasItem(sameNumber(DEFAULT_UNIT_PIECES))))
            .andExpect(jsonPath("$.[*].userQty").value(hasItem(sameNumber(DEFAULT_USER_QTY))))
            .andExpect(jsonPath("$.[*].unitQtyIn").value(hasItem(sameNumber(DEFAULT_UNIT_QTY_IN))))
            .andExpect(jsonPath("$.[*].unitQtyOut").value(hasItem(sameNumber(DEFAULT_UNIT_QTY_OUT))))
            .andExpect(jsonPath("$.[*].unitCost").value(hasItem(sameNumber(DEFAULT_UNIT_COST))))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(sameNumber(DEFAULT_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].discountPer").value(hasItem(DEFAULT_DISCOUNT_PER)))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(sameNumber(DEFAULT_DISCOUNT))))
            .andExpect(jsonPath("$.[*].totalCost").value(hasItem(sameNumber(DEFAULT_TOTAL_COST))))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].qtyIn").value(hasItem(sameNumber(DEFAULT_QTY_IN))))
            .andExpect(jsonPath("$.[*].qtyOut").value(hasItem(sameNumber(DEFAULT_QTY_OUT))))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(sameNumber(DEFAULT_COST))))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].netCost").value(hasItem(sameNumber(DEFAULT_NET_COST))))
            .andExpect(jsonPath("$.[*].netPrice").value(hasItem(sameNumber(DEFAULT_NET_PRICE))));
    }

    @Test
    void getInvoiceItem() throws Exception {
        // Initialize the database
        invoiceItem.setId(UUID.randomUUID().toString());
        invoiceItemRepository.save(invoiceItem);

        // Get the invoiceItem
        restInvoiceItemMockMvc
            .perform(get(ENTITY_API_URL_ID, invoiceItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoiceItem.getId()))
            .andExpect(jsonPath("$.pk").value(DEFAULT_PK))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT))
            .andExpect(jsonPath("$.unitPieces").value(sameNumber(DEFAULT_UNIT_PIECES)))
            .andExpect(jsonPath("$.userQty").value(sameNumber(DEFAULT_USER_QTY)))
            .andExpect(jsonPath("$.unitQtyIn").value(sameNumber(DEFAULT_UNIT_QTY_IN)))
            .andExpect(jsonPath("$.unitQtyOut").value(sameNumber(DEFAULT_UNIT_QTY_OUT)))
            .andExpect(jsonPath("$.unitCost").value(sameNumber(DEFAULT_UNIT_COST)))
            .andExpect(jsonPath("$.unitPrice").value(sameNumber(DEFAULT_UNIT_PRICE)))
            .andExpect(jsonPath("$.discountPer").value(DEFAULT_DISCOUNT_PER))
            .andExpect(jsonPath("$.discount").value(sameNumber(DEFAULT_DISCOUNT)))
            .andExpect(jsonPath("$.totalCost").value(sameNumber(DEFAULT_TOTAL_COST)))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.qtyIn").value(sameNumber(DEFAULT_QTY_IN)))
            .andExpect(jsonPath("$.qtyOut").value(sameNumber(DEFAULT_QTY_OUT)))
            .andExpect(jsonPath("$.cost").value(sameNumber(DEFAULT_COST)))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.netCost").value(sameNumber(DEFAULT_NET_COST)))
            .andExpect(jsonPath("$.netPrice").value(sameNumber(DEFAULT_NET_PRICE)));
    }

    @Test
    void getNonExistingInvoiceItem() throws Exception {
        // Get the invoiceItem
        restInvoiceItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingInvoiceItem() throws Exception {
        // Initialize the database
        invoiceItem.setId(UUID.randomUUID().toString());
        invoiceItemRepository.save(invoiceItem);

        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();

        // Update the invoiceItem
        InvoiceItem updatedInvoiceItem = invoiceItemRepository.findById(invoiceItem.getId()).get();
        updatedInvoiceItem
            .pk(UPDATED_PK)
            .unit(UPDATED_UNIT)
            .unitPieces(UPDATED_UNIT_PIECES)
            .userQty(UPDATED_USER_QTY)
            .unitQtyIn(UPDATED_UNIT_QTY_IN)
            .unitQtyOut(UPDATED_UNIT_QTY_OUT)
            .unitCost(UPDATED_UNIT_COST)
            .unitPrice(UPDATED_UNIT_PRICE)
            .discountPer(UPDATED_DISCOUNT_PER)
            .discount(UPDATED_DISCOUNT)
            .totalCost(UPDATED_TOTAL_COST)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .qtyIn(UPDATED_QTY_IN)
            .qtyOut(UPDATED_QTY_OUT)
            .cost(UPDATED_COST)
            .price(UPDATED_PRICE)
            .netCost(UPDATED_NET_COST)
            .netPrice(UPDATED_NET_PRICE);
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(updatedInvoiceItem);

        restInvoiceItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
        InvoiceItem testInvoiceItem = invoiceItemList.get(invoiceItemList.size() - 1);
        assertThat(testInvoiceItem.getPk()).isEqualTo(UPDATED_PK);
        assertThat(testInvoiceItem.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testInvoiceItem.getUnitPieces()).isEqualByComparingTo(UPDATED_UNIT_PIECES);
        assertThat(testInvoiceItem.getUserQty()).isEqualByComparingTo(UPDATED_USER_QTY);
        assertThat(testInvoiceItem.getUnitQtyIn()).isEqualByComparingTo(UPDATED_UNIT_QTY_IN);
        assertThat(testInvoiceItem.getUnitQtyOut()).isEqualByComparingTo(UPDATED_UNIT_QTY_OUT);
        assertThat(testInvoiceItem.getUnitCost()).isEqualByComparingTo(UPDATED_UNIT_COST);
        assertThat(testInvoiceItem.getUnitPrice()).isEqualByComparingTo(UPDATED_UNIT_PRICE);
        assertThat(testInvoiceItem.getDiscountPer()).isEqualTo(UPDATED_DISCOUNT_PER);
        assertThat(testInvoiceItem.getDiscount()).isEqualByComparingTo(UPDATED_DISCOUNT);
        assertThat(testInvoiceItem.getTotalCost()).isEqualByComparingTo(UPDATED_TOTAL_COST);
        assertThat(testInvoiceItem.getTotalPrice()).isEqualByComparingTo(UPDATED_TOTAL_PRICE);
        assertThat(testInvoiceItem.getQtyIn()).isEqualByComparingTo(UPDATED_QTY_IN);
        assertThat(testInvoiceItem.getQtyOut()).isEqualByComparingTo(UPDATED_QTY_OUT);
        assertThat(testInvoiceItem.getCost()).isEqualByComparingTo(UPDATED_COST);
        assertThat(testInvoiceItem.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testInvoiceItem.getNetCost()).isEqualByComparingTo(UPDATED_NET_COST);
        assertThat(testInvoiceItem.getNetPrice()).isEqualByComparingTo(UPDATED_NET_PRICE);
    }

    @Test
    void putNonExistingInvoiceItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();
        invoiceItem.setId(UUID.randomUUID().toString());

        // Create the InvoiceItem
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceItemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInvoiceItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();
        invoiceItem.setId(UUID.randomUUID().toString());

        // Create the InvoiceItem
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInvoiceItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();
        invoiceItem.setId(UUID.randomUUID().toString());

        // Create the InvoiceItem
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInvoiceItemWithPatch() throws Exception {
        // Initialize the database
        invoiceItem.setId(UUID.randomUUID().toString());
        invoiceItemRepository.save(invoiceItem);

        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();

        // Update the invoiceItem using partial update
        InvoiceItem partialUpdatedInvoiceItem = new InvoiceItem();
        partialUpdatedInvoiceItem.setId(invoiceItem.getId());

        partialUpdatedInvoiceItem
            .discount(UPDATED_DISCOUNT)
            .totalCost(UPDATED_TOTAL_COST)
            .qtyIn(UPDATED_QTY_IN)
            .qtyOut(UPDATED_QTY_OUT)
            .netPrice(UPDATED_NET_PRICE);

        restInvoiceItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoiceItem))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
        InvoiceItem testInvoiceItem = invoiceItemList.get(invoiceItemList.size() - 1);
        assertThat(testInvoiceItem.getPk()).isEqualTo(DEFAULT_PK);
        assertThat(testInvoiceItem.getUnit()).isEqualTo(DEFAULT_UNIT);
        assertThat(testInvoiceItem.getUnitPieces()).isEqualByComparingTo(DEFAULT_UNIT_PIECES);
        assertThat(testInvoiceItem.getUserQty()).isEqualByComparingTo(DEFAULT_USER_QTY);
        assertThat(testInvoiceItem.getUnitQtyIn()).isEqualByComparingTo(DEFAULT_UNIT_QTY_IN);
        assertThat(testInvoiceItem.getUnitQtyOut()).isEqualByComparingTo(DEFAULT_UNIT_QTY_OUT);
        assertThat(testInvoiceItem.getUnitCost()).isEqualByComparingTo(DEFAULT_UNIT_COST);
        assertThat(testInvoiceItem.getUnitPrice()).isEqualByComparingTo(DEFAULT_UNIT_PRICE);
        assertThat(testInvoiceItem.getDiscountPer()).isEqualTo(DEFAULT_DISCOUNT_PER);
        assertThat(testInvoiceItem.getDiscount()).isEqualByComparingTo(UPDATED_DISCOUNT);
        assertThat(testInvoiceItem.getTotalCost()).isEqualByComparingTo(UPDATED_TOTAL_COST);
        assertThat(testInvoiceItem.getTotalPrice()).isEqualByComparingTo(DEFAULT_TOTAL_PRICE);
        assertThat(testInvoiceItem.getQtyIn()).isEqualByComparingTo(UPDATED_QTY_IN);
        assertThat(testInvoiceItem.getQtyOut()).isEqualByComparingTo(UPDATED_QTY_OUT);
        assertThat(testInvoiceItem.getCost()).isEqualByComparingTo(DEFAULT_COST);
        assertThat(testInvoiceItem.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testInvoiceItem.getNetCost()).isEqualByComparingTo(DEFAULT_NET_COST);
        assertThat(testInvoiceItem.getNetPrice()).isEqualByComparingTo(UPDATED_NET_PRICE);
    }

    @Test
    void fullUpdateInvoiceItemWithPatch() throws Exception {
        // Initialize the database
        invoiceItem.setId(UUID.randomUUID().toString());
        invoiceItemRepository.save(invoiceItem);

        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();

        // Update the invoiceItem using partial update
        InvoiceItem partialUpdatedInvoiceItem = new InvoiceItem();
        partialUpdatedInvoiceItem.setId(invoiceItem.getId());

        partialUpdatedInvoiceItem
            .pk(UPDATED_PK)
            .unit(UPDATED_UNIT)
            .unitPieces(UPDATED_UNIT_PIECES)
            .userQty(UPDATED_USER_QTY)
            .unitQtyIn(UPDATED_UNIT_QTY_IN)
            .unitQtyOut(UPDATED_UNIT_QTY_OUT)
            .unitCost(UPDATED_UNIT_COST)
            .unitPrice(UPDATED_UNIT_PRICE)
            .discountPer(UPDATED_DISCOUNT_PER)
            .discount(UPDATED_DISCOUNT)
            .totalCost(UPDATED_TOTAL_COST)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .qtyIn(UPDATED_QTY_IN)
            .qtyOut(UPDATED_QTY_OUT)
            .cost(UPDATED_COST)
            .price(UPDATED_PRICE)
            .netCost(UPDATED_NET_COST)
            .netPrice(UPDATED_NET_PRICE);

        restInvoiceItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoiceItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoiceItem))
            )
            .andExpect(status().isOk());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
        InvoiceItem testInvoiceItem = invoiceItemList.get(invoiceItemList.size() - 1);
        assertThat(testInvoiceItem.getPk()).isEqualTo(UPDATED_PK);
        assertThat(testInvoiceItem.getUnit()).isEqualTo(UPDATED_UNIT);
        assertThat(testInvoiceItem.getUnitPieces()).isEqualByComparingTo(UPDATED_UNIT_PIECES);
        assertThat(testInvoiceItem.getUserQty()).isEqualByComparingTo(UPDATED_USER_QTY);
        assertThat(testInvoiceItem.getUnitQtyIn()).isEqualByComparingTo(UPDATED_UNIT_QTY_IN);
        assertThat(testInvoiceItem.getUnitQtyOut()).isEqualByComparingTo(UPDATED_UNIT_QTY_OUT);
        assertThat(testInvoiceItem.getUnitCost()).isEqualByComparingTo(UPDATED_UNIT_COST);
        assertThat(testInvoiceItem.getUnitPrice()).isEqualByComparingTo(UPDATED_UNIT_PRICE);
        assertThat(testInvoiceItem.getDiscountPer()).isEqualTo(UPDATED_DISCOUNT_PER);
        assertThat(testInvoiceItem.getDiscount()).isEqualByComparingTo(UPDATED_DISCOUNT);
        assertThat(testInvoiceItem.getTotalCost()).isEqualByComparingTo(UPDATED_TOTAL_COST);
        assertThat(testInvoiceItem.getTotalPrice()).isEqualByComparingTo(UPDATED_TOTAL_PRICE);
        assertThat(testInvoiceItem.getQtyIn()).isEqualByComparingTo(UPDATED_QTY_IN);
        assertThat(testInvoiceItem.getQtyOut()).isEqualByComparingTo(UPDATED_QTY_OUT);
        assertThat(testInvoiceItem.getCost()).isEqualByComparingTo(UPDATED_COST);
        assertThat(testInvoiceItem.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testInvoiceItem.getNetCost()).isEqualByComparingTo(UPDATED_NET_COST);
        assertThat(testInvoiceItem.getNetPrice()).isEqualByComparingTo(UPDATED_NET_PRICE);
    }

    @Test
    void patchNonExistingInvoiceItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();
        invoiceItem.setId(UUID.randomUUID().toString());

        // Create the InvoiceItem
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceItemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInvoiceItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();
        invoiceItem.setId(UUID.randomUUID().toString());

        // Create the InvoiceItem
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInvoiceItem() throws Exception {
        int databaseSizeBeforeUpdate = invoiceItemRepository.findAll().size();
        invoiceItem.setId(UUID.randomUUID().toString());

        // Create the InvoiceItem
        InvoiceItemDTO invoiceItemDTO = invoiceItemMapper.toDto(invoiceItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceItemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(invoiceItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvoiceItem in the database
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInvoiceItem() throws Exception {
        // Initialize the database
        invoiceItem.setId(UUID.randomUUID().toString());
        invoiceItemRepository.save(invoiceItem);

        int databaseSizeBeforeDelete = invoiceItemRepository.findAll().size();

        // Delete the invoiceItem
        restInvoiceItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoiceItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<InvoiceItem> invoiceItemList = invoiceItemRepository.findAll();
        assertThat(invoiceItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
