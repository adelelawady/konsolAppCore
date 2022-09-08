package com.konsol.core.web.rest;

import static com.konsol.core.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.konsol.core.IntegrationTest;
import com.konsol.core.domain.Money;
import com.konsol.core.domain.enumeration.MoneyKind;
import com.konsol.core.repository.MoneyRepository;
import com.konsol.core.service.dto.MoneyDTO;
import com.konsol.core.service.mapper.MoneyMapper;
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
 * Integration tests for the {@link MoneyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MoneyResourceIT {

    private static final String DEFAULT_PK = "AAAAAAAAAA";
    private static final String UPDATED_PK = "BBBBBBBBBB";

    private static final MoneyKind DEFAULT_KIND = MoneyKind.PAYMENT;
    private static final MoneyKind UPDATED_KIND = MoneyKind.RECEIPT;

    private static final BigDecimal DEFAULT_MONEY_IN = new BigDecimal(0);
    private static final BigDecimal UPDATED_MONEY_IN = new BigDecimal(1);

    private static final BigDecimal DEFAULT_MONEY_OUT = new BigDecimal(0);
    private static final BigDecimal UPDATED_MONEY_OUT = new BigDecimal(1);

    private static final String ENTITY_API_URL = "/api/monies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private MoneyRepository moneyRepository;

    @Autowired
    private MoneyMapper moneyMapper;

    @Autowired
    private MockMvc restMoneyMockMvc;

    private Money money;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Money createEntity() {
        Money money = new Money().pk(DEFAULT_PK).kind(DEFAULT_KIND).moneyIn(DEFAULT_MONEY_IN).moneyOut(DEFAULT_MONEY_OUT);
        return money;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Money createUpdatedEntity() {
        Money money = new Money().pk(UPDATED_PK).kind(UPDATED_KIND).moneyIn(UPDATED_MONEY_IN).moneyOut(UPDATED_MONEY_OUT);
        return money;
    }

    @BeforeEach
    public void initTest() {
        moneyRepository.deleteAll();
        money = createEntity();
    }

    @Test
    void createMoney() throws Exception {
        int databaseSizeBeforeCreate = moneyRepository.findAll().size();
        // Create the Money
        MoneyDTO moneyDTO = moneyMapper.toDto(money);
        restMoneyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moneyDTO)))
            .andExpect(status().isCreated());

        // Validate the Money in the database
        List<Money> moneyList = moneyRepository.findAll();
        assertThat(moneyList).hasSize(databaseSizeBeforeCreate + 1);
        Money testMoney = moneyList.get(moneyList.size() - 1);
        assertThat(testMoney.getPk()).isEqualTo(DEFAULT_PK);
        assertThat(testMoney.getKind()).isEqualTo(DEFAULT_KIND);
        assertThat(testMoney.getMoneyIn()).isEqualByComparingTo(DEFAULT_MONEY_IN);
        assertThat(testMoney.getMoneyOut()).isEqualByComparingTo(DEFAULT_MONEY_OUT);
    }

    @Test
    void createMoneyWithExistingId() throws Exception {
        // Create the Money with an existing ID
        money.setId("existing_id");
        MoneyDTO moneyDTO = moneyMapper.toDto(money);

        int databaseSizeBeforeCreate = moneyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMoneyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moneyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Money in the database
        List<Money> moneyList = moneyRepository.findAll();
        assertThat(moneyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllMonies() throws Exception {
        // Initialize the database
        money.setId(UUID.randomUUID().toString());
        moneyRepository.save(money);

        // Get all the moneyList
        restMoneyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(money.getId())))
            .andExpect(jsonPath("$.[*].pk").value(hasItem(DEFAULT_PK)))
            .andExpect(jsonPath("$.[*].kind").value(hasItem(DEFAULT_KIND.toString())))
            .andExpect(jsonPath("$.[*].moneyIn").value(hasItem(sameNumber(DEFAULT_MONEY_IN))))
            .andExpect(jsonPath("$.[*].moneyOut").value(hasItem(sameNumber(DEFAULT_MONEY_OUT))));
    }

    @Test
    void getMoney() throws Exception {
        // Initialize the database
        money.setId(UUID.randomUUID().toString());
        moneyRepository.save(money);

        // Get the money
        restMoneyMockMvc
            .perform(get(ENTITY_API_URL_ID, money.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(money.getId()))
            .andExpect(jsonPath("$.pk").value(DEFAULT_PK))
            .andExpect(jsonPath("$.kind").value(DEFAULT_KIND.toString()))
            .andExpect(jsonPath("$.moneyIn").value(sameNumber(DEFAULT_MONEY_IN)))
            .andExpect(jsonPath("$.moneyOut").value(sameNumber(DEFAULT_MONEY_OUT)));
    }

    @Test
    void getNonExistingMoney() throws Exception {
        // Get the money
        restMoneyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingMoney() throws Exception {
        // Initialize the database
        money.setId(UUID.randomUUID().toString());
        moneyRepository.save(money);

        int databaseSizeBeforeUpdate = moneyRepository.findAll().size();

        // Update the money
        Money updatedMoney = moneyRepository.findById(money.getId()).get();
        updatedMoney.pk(UPDATED_PK).kind(UPDATED_KIND).moneyIn(UPDATED_MONEY_IN).moneyOut(UPDATED_MONEY_OUT);
        MoneyDTO moneyDTO = moneyMapper.toDto(updatedMoney);

        restMoneyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moneyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(moneyDTO))
            )
            .andExpect(status().isOk());

        // Validate the Money in the database
        List<Money> moneyList = moneyRepository.findAll();
        assertThat(moneyList).hasSize(databaseSizeBeforeUpdate);
        Money testMoney = moneyList.get(moneyList.size() - 1);
        assertThat(testMoney.getPk()).isEqualTo(UPDATED_PK);
        assertThat(testMoney.getKind()).isEqualTo(UPDATED_KIND);
        assertThat(testMoney.getMoneyIn()).isEqualByComparingTo(UPDATED_MONEY_IN);
        assertThat(testMoney.getMoneyOut()).isEqualByComparingTo(UPDATED_MONEY_OUT);
    }

    @Test
    void putNonExistingMoney() throws Exception {
        int databaseSizeBeforeUpdate = moneyRepository.findAll().size();
        money.setId(UUID.randomUUID().toString());

        // Create the Money
        MoneyDTO moneyDTO = moneyMapper.toDto(money);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoneyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moneyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(moneyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Money in the database
        List<Money> moneyList = moneyRepository.findAll();
        assertThat(moneyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchMoney() throws Exception {
        int databaseSizeBeforeUpdate = moneyRepository.findAll().size();
        money.setId(UUID.randomUUID().toString());

        // Create the Money
        MoneyDTO moneyDTO = moneyMapper.toDto(money);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(moneyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Money in the database
        List<Money> moneyList = moneyRepository.findAll();
        assertThat(moneyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamMoney() throws Exception {
        int databaseSizeBeforeUpdate = moneyRepository.findAll().size();
        money.setId(UUID.randomUUID().toString());

        // Create the Money
        MoneyDTO moneyDTO = moneyMapper.toDto(money);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(moneyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Money in the database
        List<Money> moneyList = moneyRepository.findAll();
        assertThat(moneyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateMoneyWithPatch() throws Exception {
        // Initialize the database
        money.setId(UUID.randomUUID().toString());
        moneyRepository.save(money);

        int databaseSizeBeforeUpdate = moneyRepository.findAll().size();

        // Update the money using partial update
        Money partialUpdatedMoney = new Money();
        partialUpdatedMoney.setId(money.getId());

        partialUpdatedMoney.pk(UPDATED_PK).kind(UPDATED_KIND).moneyIn(UPDATED_MONEY_IN);

        restMoneyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoney.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMoney))
            )
            .andExpect(status().isOk());

        // Validate the Money in the database
        List<Money> moneyList = moneyRepository.findAll();
        assertThat(moneyList).hasSize(databaseSizeBeforeUpdate);
        Money testMoney = moneyList.get(moneyList.size() - 1);
        assertThat(testMoney.getPk()).isEqualTo(UPDATED_PK);
        assertThat(testMoney.getKind()).isEqualTo(UPDATED_KIND);
        assertThat(testMoney.getMoneyIn()).isEqualByComparingTo(UPDATED_MONEY_IN);
        assertThat(testMoney.getMoneyOut()).isEqualByComparingTo(DEFAULT_MONEY_OUT);
    }

    @Test
    void fullUpdateMoneyWithPatch() throws Exception {
        // Initialize the database
        money.setId(UUID.randomUUID().toString());
        moneyRepository.save(money);

        int databaseSizeBeforeUpdate = moneyRepository.findAll().size();

        // Update the money using partial update
        Money partialUpdatedMoney = new Money();
        partialUpdatedMoney.setId(money.getId());

        partialUpdatedMoney.pk(UPDATED_PK).kind(UPDATED_KIND).moneyIn(UPDATED_MONEY_IN).moneyOut(UPDATED_MONEY_OUT);

        restMoneyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoney.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMoney))
            )
            .andExpect(status().isOk());

        // Validate the Money in the database
        List<Money> moneyList = moneyRepository.findAll();
        assertThat(moneyList).hasSize(databaseSizeBeforeUpdate);
        Money testMoney = moneyList.get(moneyList.size() - 1);
        assertThat(testMoney.getPk()).isEqualTo(UPDATED_PK);
        assertThat(testMoney.getKind()).isEqualTo(UPDATED_KIND);
        assertThat(testMoney.getMoneyIn()).isEqualByComparingTo(UPDATED_MONEY_IN);
        assertThat(testMoney.getMoneyOut()).isEqualByComparingTo(UPDATED_MONEY_OUT);
    }

    @Test
    void patchNonExistingMoney() throws Exception {
        int databaseSizeBeforeUpdate = moneyRepository.findAll().size();
        money.setId(UUID.randomUUID().toString());

        // Create the Money
        MoneyDTO moneyDTO = moneyMapper.toDto(money);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoneyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, moneyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(moneyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Money in the database
        List<Money> moneyList = moneyRepository.findAll();
        assertThat(moneyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchMoney() throws Exception {
        int databaseSizeBeforeUpdate = moneyRepository.findAll().size();
        money.setId(UUID.randomUUID().toString());

        // Create the Money
        MoneyDTO moneyDTO = moneyMapper.toDto(money);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(moneyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Money in the database
        List<Money> moneyList = moneyRepository.findAll();
        assertThat(moneyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamMoney() throws Exception {
        int databaseSizeBeforeUpdate = moneyRepository.findAll().size();
        money.setId(UUID.randomUUID().toString());

        // Create the Money
        MoneyDTO moneyDTO = moneyMapper.toDto(money);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoneyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(moneyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Money in the database
        List<Money> moneyList = moneyRepository.findAll();
        assertThat(moneyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteMoney() throws Exception {
        // Initialize the database
        money.setId(UUID.randomUUID().toString());
        moneyRepository.save(money);

        int databaseSizeBeforeDelete = moneyRepository.findAll().size();

        // Delete the money
        restMoneyMockMvc
            .perform(delete(ENTITY_API_URL_ID, money.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Money> moneyList = moneyRepository.findAll();
        assertThat(moneyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
