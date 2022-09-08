package com.konsol.core.web.rest;

import static com.konsol.core.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.konsol.core.IntegrationTest;
import com.konsol.core.domain.Invoice;
import com.konsol.core.domain.enumeration.InvoiceKind;
import com.konsol.core.repository.InvoiceRepository;
import com.konsol.core.service.InvoiceService;
import com.konsol.core.service.dto.InvoiceDTO;
import com.konsol.core.service.mapper.InvoiceMapper;
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
 * Integration tests for the {@link InvoiceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InvoiceResourceIT {

    private static final String DEFAULT_PK = "AAAAAAAAAA";
    private static final String UPDATED_PK = "BBBBBBBBBB";

    private static final InvoiceKind DEFAULT_KIND = InvoiceKind.SALE;
    private static final InvoiceKind UPDATED_KIND = InvoiceKind.PURCHASE;

    private static final BigDecimal DEFAULT_TOTAL_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_COST = new BigDecimal(1);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(1);

    private static final Integer DEFAULT_DISCOUNT_PER = 0;
    private static final Integer UPDATED_DISCOUNT_PER = 1;

    private static final BigDecimal DEFAULT_DISCOUNT = new BigDecimal(0);
    private static final BigDecimal UPDATED_DISCOUNT = new BigDecimal(1);

    private static final BigDecimal DEFAULT_ADDITIONS = new BigDecimal(0);
    private static final BigDecimal UPDATED_ADDITIONS = new BigDecimal(1);

    private static final String DEFAULT_ADDITIONS_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ADDITIONS_TYPE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_NET_COST = new BigDecimal(0);
    private static final BigDecimal UPDATED_NET_COST = new BigDecimal(1);

    private static final BigDecimal DEFAULT_NET_PRICE = new BigDecimal(0);
    private static final BigDecimal UPDATED_NET_PRICE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_EXPENSES = new BigDecimal(0);
    private static final BigDecimal UPDATED_EXPENSES = new BigDecimal(1);

    private static final String DEFAULT_EXPENSES_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_EXPENSES_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/invoices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Mock
    private InvoiceRepository invoiceRepositoryMock;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Mock
    private InvoiceService invoiceServiceMock;

    @Autowired
    private MockMvc restInvoiceMockMvc;

    private Invoice invoice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createEntity() {
        Invoice invoice = new Invoice()
            .pk(DEFAULT_PK)
            .kind(DEFAULT_KIND)
            .totalCost(DEFAULT_TOTAL_COST)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .discountPer(DEFAULT_DISCOUNT_PER)
            .discount(DEFAULT_DISCOUNT)
            .additions(DEFAULT_ADDITIONS)
            .additionsType(DEFAULT_ADDITIONS_TYPE)
            .netCost(DEFAULT_NET_COST)
            .netPrice(DEFAULT_NET_PRICE)
            .expenses(DEFAULT_EXPENSES)
            .expensesType(DEFAULT_EXPENSES_TYPE);
        return invoice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Invoice createUpdatedEntity() {
        Invoice invoice = new Invoice()
            .pk(UPDATED_PK)
            .kind(UPDATED_KIND)
            .totalCost(UPDATED_TOTAL_COST)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .discountPer(UPDATED_DISCOUNT_PER)
            .discount(UPDATED_DISCOUNT)
            .additions(UPDATED_ADDITIONS)
            .additionsType(UPDATED_ADDITIONS_TYPE)
            .netCost(UPDATED_NET_COST)
            .netPrice(UPDATED_NET_PRICE)
            .expenses(UPDATED_EXPENSES)
            .expensesType(UPDATED_EXPENSES_TYPE);
        return invoice;
    }

    @BeforeEach
    public void initTest() {
        invoiceRepository.deleteAll();
        invoice = createEntity();
    }

    @Test
    void createInvoice() throws Exception {
        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();
        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);
        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isCreated());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate + 1);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getPk()).isEqualTo(DEFAULT_PK);
        assertThat(testInvoice.getKind()).isEqualTo(DEFAULT_KIND);
        assertThat(testInvoice.getTotalCost()).isEqualByComparingTo(DEFAULT_TOTAL_COST);
        assertThat(testInvoice.getTotalPrice()).isEqualByComparingTo(DEFAULT_TOTAL_PRICE);
        assertThat(testInvoice.getDiscountPer()).isEqualTo(DEFAULT_DISCOUNT_PER);
        assertThat(testInvoice.getDiscount()).isEqualByComparingTo(DEFAULT_DISCOUNT);
        assertThat(testInvoice.getAdditions()).isEqualByComparingTo(DEFAULT_ADDITIONS);
        assertThat(testInvoice.getAdditionsType()).isEqualTo(DEFAULT_ADDITIONS_TYPE);
        assertThat(testInvoice.getNetCost()).isEqualByComparingTo(DEFAULT_NET_COST);
        assertThat(testInvoice.getNetPrice()).isEqualByComparingTo(DEFAULT_NET_PRICE);
        assertThat(testInvoice.getExpenses()).isEqualByComparingTo(DEFAULT_EXPENSES);
        assertThat(testInvoice.getExpensesType()).isEqualTo(DEFAULT_EXPENSES_TYPE);
    }

    @Test
    void createInvoiceWithExistingId() throws Exception {
        // Create the Invoice with an existing ID
        invoice.setId("existing_id");
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        int databaseSizeBeforeCreate = invoiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllInvoices() throws Exception {
        // Initialize the database
        invoice.setId(UUID.randomUUID().toString());
        invoiceRepository.save(invoice);

        // Get all the invoiceList
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(invoice.getId())))
            .andExpect(jsonPath("$.[*].pk").value(hasItem(DEFAULT_PK)))
            .andExpect(jsonPath("$.[*].kind").value(hasItem(DEFAULT_KIND.toString())))
            .andExpect(jsonPath("$.[*].totalCost").value(hasItem(sameNumber(DEFAULT_TOTAL_COST))))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].discountPer").value(hasItem(DEFAULT_DISCOUNT_PER)))
            .andExpect(jsonPath("$.[*].discount").value(hasItem(sameNumber(DEFAULT_DISCOUNT))))
            .andExpect(jsonPath("$.[*].additions").value(hasItem(sameNumber(DEFAULT_ADDITIONS))))
            .andExpect(jsonPath("$.[*].additionsType").value(hasItem(DEFAULT_ADDITIONS_TYPE)))
            .andExpect(jsonPath("$.[*].netCost").value(hasItem(sameNumber(DEFAULT_NET_COST))))
            .andExpect(jsonPath("$.[*].netPrice").value(hasItem(sameNumber(DEFAULT_NET_PRICE))))
            .andExpect(jsonPath("$.[*].expenses").value(hasItem(sameNumber(DEFAULT_EXPENSES))))
            .andExpect(jsonPath("$.[*].expensesType").value(hasItem(DEFAULT_EXPENSES_TYPE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInvoicesWithEagerRelationshipsIsEnabled() throws Exception {
        when(invoiceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInvoiceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(invoiceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInvoicesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(invoiceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInvoiceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(invoiceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void getInvoice() throws Exception {
        // Initialize the database
        invoice.setId(UUID.randomUUID().toString());
        invoiceRepository.save(invoice);

        // Get the invoice
        restInvoiceMockMvc
            .perform(get(ENTITY_API_URL_ID, invoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(invoice.getId()))
            .andExpect(jsonPath("$.pk").value(DEFAULT_PK))
            .andExpect(jsonPath("$.kind").value(DEFAULT_KIND.toString()))
            .andExpect(jsonPath("$.totalCost").value(sameNumber(DEFAULT_TOTAL_COST)))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.discountPer").value(DEFAULT_DISCOUNT_PER))
            .andExpect(jsonPath("$.discount").value(sameNumber(DEFAULT_DISCOUNT)))
            .andExpect(jsonPath("$.additions").value(sameNumber(DEFAULT_ADDITIONS)))
            .andExpect(jsonPath("$.additionsType").value(DEFAULT_ADDITIONS_TYPE))
            .andExpect(jsonPath("$.netCost").value(sameNumber(DEFAULT_NET_COST)))
            .andExpect(jsonPath("$.netPrice").value(sameNumber(DEFAULT_NET_PRICE)))
            .andExpect(jsonPath("$.expenses").value(sameNumber(DEFAULT_EXPENSES)))
            .andExpect(jsonPath("$.expensesType").value(DEFAULT_EXPENSES_TYPE));
    }

    @Test
    void getNonExistingInvoice() throws Exception {
        // Get the invoice
        restInvoiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingInvoice() throws Exception {
        // Initialize the database
        invoice.setId(UUID.randomUUID().toString());
        invoiceRepository.save(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice
        Invoice updatedInvoice = invoiceRepository.findById(invoice.getId()).get();
        updatedInvoice
            .pk(UPDATED_PK)
            .kind(UPDATED_KIND)
            .totalCost(UPDATED_TOTAL_COST)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .discountPer(UPDATED_DISCOUNT_PER)
            .discount(UPDATED_DISCOUNT)
            .additions(UPDATED_ADDITIONS)
            .additionsType(UPDATED_ADDITIONS_TYPE)
            .netCost(UPDATED_NET_COST)
            .netPrice(UPDATED_NET_PRICE)
            .expenses(UPDATED_EXPENSES)
            .expensesType(UPDATED_EXPENSES_TYPE);
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(updatedInvoice);

        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getPk()).isEqualTo(UPDATED_PK);
        assertThat(testInvoice.getKind()).isEqualTo(UPDATED_KIND);
        assertThat(testInvoice.getTotalCost()).isEqualByComparingTo(UPDATED_TOTAL_COST);
        assertThat(testInvoice.getTotalPrice()).isEqualByComparingTo(UPDATED_TOTAL_PRICE);
        assertThat(testInvoice.getDiscountPer()).isEqualTo(UPDATED_DISCOUNT_PER);
        assertThat(testInvoice.getDiscount()).isEqualByComparingTo(UPDATED_DISCOUNT);
        assertThat(testInvoice.getAdditions()).isEqualByComparingTo(UPDATED_ADDITIONS);
        assertThat(testInvoice.getAdditionsType()).isEqualTo(UPDATED_ADDITIONS_TYPE);
        assertThat(testInvoice.getNetCost()).isEqualByComparingTo(UPDATED_NET_COST);
        assertThat(testInvoice.getNetPrice()).isEqualByComparingTo(UPDATED_NET_PRICE);
        assertThat(testInvoice.getExpenses()).isEqualByComparingTo(UPDATED_EXPENSES);
        assertThat(testInvoice.getExpensesType()).isEqualTo(UPDATED_EXPENSES_TYPE);
    }

    @Test
    void putNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(UUID.randomUUID().toString());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, invoiceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(UUID.randomUUID().toString());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(UUID.randomUUID().toString());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(invoiceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateInvoiceWithPatch() throws Exception {
        // Initialize the database
        invoice.setId(UUID.randomUUID().toString());
        invoiceRepository.save(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice using partial update
        Invoice partialUpdatedInvoice = new Invoice();
        partialUpdatedInvoice.setId(invoice.getId());

        partialUpdatedInvoice
            .totalCost(UPDATED_TOTAL_COST)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .discountPer(UPDATED_DISCOUNT_PER)
            .netCost(UPDATED_NET_COST)
            .netPrice(UPDATED_NET_PRICE)
            .expenses(UPDATED_EXPENSES);

        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoice))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getPk()).isEqualTo(DEFAULT_PK);
        assertThat(testInvoice.getKind()).isEqualTo(DEFAULT_KIND);
        assertThat(testInvoice.getTotalCost()).isEqualByComparingTo(UPDATED_TOTAL_COST);
        assertThat(testInvoice.getTotalPrice()).isEqualByComparingTo(UPDATED_TOTAL_PRICE);
        assertThat(testInvoice.getDiscountPer()).isEqualTo(UPDATED_DISCOUNT_PER);
        assertThat(testInvoice.getDiscount()).isEqualByComparingTo(DEFAULT_DISCOUNT);
        assertThat(testInvoice.getAdditions()).isEqualByComparingTo(DEFAULT_ADDITIONS);
        assertThat(testInvoice.getAdditionsType()).isEqualTo(DEFAULT_ADDITIONS_TYPE);
        assertThat(testInvoice.getNetCost()).isEqualByComparingTo(UPDATED_NET_COST);
        assertThat(testInvoice.getNetPrice()).isEqualByComparingTo(UPDATED_NET_PRICE);
        assertThat(testInvoice.getExpenses()).isEqualByComparingTo(UPDATED_EXPENSES);
        assertThat(testInvoice.getExpensesType()).isEqualTo(DEFAULT_EXPENSES_TYPE);
    }

    @Test
    void fullUpdateInvoiceWithPatch() throws Exception {
        // Initialize the database
        invoice.setId(UUID.randomUUID().toString());
        invoiceRepository.save(invoice);

        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();

        // Update the invoice using partial update
        Invoice partialUpdatedInvoice = new Invoice();
        partialUpdatedInvoice.setId(invoice.getId());

        partialUpdatedInvoice
            .pk(UPDATED_PK)
            .kind(UPDATED_KIND)
            .totalCost(UPDATED_TOTAL_COST)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .discountPer(UPDATED_DISCOUNT_PER)
            .discount(UPDATED_DISCOUNT)
            .additions(UPDATED_ADDITIONS)
            .additionsType(UPDATED_ADDITIONS_TYPE)
            .netCost(UPDATED_NET_COST)
            .netPrice(UPDATED_NET_PRICE)
            .expenses(UPDATED_EXPENSES)
            .expensesType(UPDATED_EXPENSES_TYPE);

        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedInvoice))
            )
            .andExpect(status().isOk());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
        Invoice testInvoice = invoiceList.get(invoiceList.size() - 1);
        assertThat(testInvoice.getPk()).isEqualTo(UPDATED_PK);
        assertThat(testInvoice.getKind()).isEqualTo(UPDATED_KIND);
        assertThat(testInvoice.getTotalCost()).isEqualByComparingTo(UPDATED_TOTAL_COST);
        assertThat(testInvoice.getTotalPrice()).isEqualByComparingTo(UPDATED_TOTAL_PRICE);
        assertThat(testInvoice.getDiscountPer()).isEqualTo(UPDATED_DISCOUNT_PER);
        assertThat(testInvoice.getDiscount()).isEqualByComparingTo(UPDATED_DISCOUNT);
        assertThat(testInvoice.getAdditions()).isEqualByComparingTo(UPDATED_ADDITIONS);
        assertThat(testInvoice.getAdditionsType()).isEqualTo(UPDATED_ADDITIONS_TYPE);
        assertThat(testInvoice.getNetCost()).isEqualByComparingTo(UPDATED_NET_COST);
        assertThat(testInvoice.getNetPrice()).isEqualByComparingTo(UPDATED_NET_PRICE);
        assertThat(testInvoice.getExpenses()).isEqualByComparingTo(UPDATED_EXPENSES);
        assertThat(testInvoice.getExpensesType()).isEqualTo(UPDATED_EXPENSES_TYPE);
    }

    @Test
    void patchNonExistingInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(UUID.randomUUID().toString());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, invoiceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(UUID.randomUUID().toString());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamInvoice() throws Exception {
        int databaseSizeBeforeUpdate = invoiceRepository.findAll().size();
        invoice.setId(UUID.randomUUID().toString());

        // Create the Invoice
        InvoiceDTO invoiceDTO = invoiceMapper.toDto(invoice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(invoiceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Invoice in the database
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteInvoice() throws Exception {
        // Initialize the database
        invoice.setId(UUID.randomUUID().toString());
        invoiceRepository.save(invoice);

        int databaseSizeBeforeDelete = invoiceRepository.findAll().size();

        // Delete the invoice
        restInvoiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, invoice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
