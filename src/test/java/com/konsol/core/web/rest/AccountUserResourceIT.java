package com.konsol.core.web.rest;

import static com.konsol.core.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.konsol.core.IntegrationTest;
import com.konsol.core.domain.AccountUser;
import com.konsol.core.domain.enumeration.AccountKind;
import com.konsol.core.repository.AccountUserRepository;
import com.konsol.core.service.dto.AccountUserDTO;
import com.konsol.core.service.mapper.AccountUserMapper;
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
 * Integration tests for the {@link AccountUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccountUserResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final AccountKind DEFAULT_KIND = AccountKind.CUSTOMER;
    private static final AccountKind UPDATED_KIND = AccountKind.SUPPLIER;

    private static final BigDecimal DEFAULT_BALANCE_IN = new BigDecimal(0);
    private static final BigDecimal UPDATED_BALANCE_IN = new BigDecimal(1);

    private static final BigDecimal DEFAULT_BALANCE_OUT = new BigDecimal(0);
    private static final BigDecimal UPDATED_BALANCE_OUT = new BigDecimal(1);

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_2 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/account-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private AccountUserRepository accountUserRepository;

    @Autowired
    private AccountUserMapper accountUserMapper;

    @Autowired
    private MockMvc restAccountUserMockMvc;

    private AccountUser accountUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountUser createEntity() {
        AccountUser accountUser = new AccountUser()
            .name(DEFAULT_NAME)
            .kind(DEFAULT_KIND)
            .balanceIn(DEFAULT_BALANCE_IN)
            .balanceOut(DEFAULT_BALANCE_OUT)
            .phone(DEFAULT_PHONE)
            .address(DEFAULT_ADDRESS)
            .address2(DEFAULT_ADDRESS_2);
        return accountUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountUser createUpdatedEntity() {
        AccountUser accountUser = new AccountUser()
            .name(UPDATED_NAME)
            .kind(UPDATED_KIND)
            .balanceIn(UPDATED_BALANCE_IN)
            .balanceOut(UPDATED_BALANCE_OUT)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .address2(UPDATED_ADDRESS_2);
        return accountUser;
    }

    @BeforeEach
    public void initTest() {
        accountUserRepository.deleteAll();
        accountUser = createEntity();
    }

    @Test
    void createAccountUser() throws Exception {
        int databaseSizeBeforeCreate = accountUserRepository.findAll().size();
        // Create the AccountUser
        AccountUserDTO accountUserDTO = accountUserMapper.toDto(accountUser);
        restAccountUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountUserDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AccountUser in the database
        List<AccountUser> accountUserList = accountUserRepository.findAll();
        assertThat(accountUserList).hasSize(databaseSizeBeforeCreate + 1);
        AccountUser testAccountUser = accountUserList.get(accountUserList.size() - 1);
        assertThat(testAccountUser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAccountUser.getKind()).isEqualTo(DEFAULT_KIND);
        assertThat(testAccountUser.getBalanceIn()).isEqualByComparingTo(DEFAULT_BALANCE_IN);
        assertThat(testAccountUser.getBalanceOut()).isEqualByComparingTo(DEFAULT_BALANCE_OUT);
        assertThat(testAccountUser.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testAccountUser.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testAccountUser.getAddress2()).isEqualTo(DEFAULT_ADDRESS_2);
    }

    @Test
    void createAccountUserWithExistingId() throws Exception {
        // Create the AccountUser with an existing ID
        accountUser.setId("existing_id");
        AccountUserDTO accountUserDTO = accountUserMapper.toDto(accountUser);

        int databaseSizeBeforeCreate = accountUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountUser in the database
        List<AccountUser> accountUserList = accountUserRepository.findAll();
        assertThat(accountUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = accountUserRepository.findAll().size();
        // set the field null
        accountUser.setName(null);

        // Create the AccountUser, which fails.
        AccountUserDTO accountUserDTO = accountUserMapper.toDto(accountUser);

        restAccountUserMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountUserDTO))
            )
            .andExpect(status().isBadRequest());

        List<AccountUser> accountUserList = accountUserRepository.findAll();
        assertThat(accountUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    void getAllAccountUsers() throws Exception {
        // Initialize the database
        accountUser.setId(UUID.randomUUID().toString());
        accountUserRepository.save(accountUser);

        // Get all the accountUserList
        restAccountUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountUser.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].kind").value(hasItem(DEFAULT_KIND.toString())))
            .andExpect(jsonPath("$.[*].balanceIn").value(hasItem(sameNumber(DEFAULT_BALANCE_IN))))
            .andExpect(jsonPath("$.[*].balanceOut").value(hasItem(sameNumber(DEFAULT_BALANCE_OUT))))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2)));
    }

    @Test
    void getAccountUser() throws Exception {
        // Initialize the database
        accountUser.setId(UUID.randomUUID().toString());
        accountUserRepository.save(accountUser);

        // Get the accountUser
        restAccountUserMockMvc
            .perform(get(ENTITY_API_URL_ID, accountUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountUser.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.kind").value(DEFAULT_KIND.toString()))
            .andExpect(jsonPath("$.balanceIn").value(sameNumber(DEFAULT_BALANCE_IN)))
            .andExpect(jsonPath("$.balanceOut").value(sameNumber(DEFAULT_BALANCE_OUT)))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.address2").value(DEFAULT_ADDRESS_2));
    }

    @Test
    void getNonExistingAccountUser() throws Exception {
        // Get the accountUser
        restAccountUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingAccountUser() throws Exception {
        // Initialize the database
        accountUser.setId(UUID.randomUUID().toString());
        accountUserRepository.save(accountUser);

        int databaseSizeBeforeUpdate = accountUserRepository.findAll().size();

        // Update the accountUser
        AccountUser updatedAccountUser = accountUserRepository.findById(accountUser.getId()).get();
        updatedAccountUser
            .name(UPDATED_NAME)
            .kind(UPDATED_KIND)
            .balanceIn(UPDATED_BALANCE_IN)
            .balanceOut(UPDATED_BALANCE_OUT)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .address2(UPDATED_ADDRESS_2);
        AccountUserDTO accountUserDTO = accountUserMapper.toDto(updatedAccountUser);

        restAccountUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountUserDTO))
            )
            .andExpect(status().isOk());

        // Validate the AccountUser in the database
        List<AccountUser> accountUserList = accountUserRepository.findAll();
        assertThat(accountUserList).hasSize(databaseSizeBeforeUpdate);
        AccountUser testAccountUser = accountUserList.get(accountUserList.size() - 1);
        assertThat(testAccountUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAccountUser.getKind()).isEqualTo(UPDATED_KIND);
        assertThat(testAccountUser.getBalanceIn()).isEqualByComparingTo(UPDATED_BALANCE_IN);
        assertThat(testAccountUser.getBalanceOut()).isEqualByComparingTo(UPDATED_BALANCE_OUT);
        assertThat(testAccountUser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testAccountUser.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testAccountUser.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
    }

    @Test
    void putNonExistingAccountUser() throws Exception {
        int databaseSizeBeforeUpdate = accountUserRepository.findAll().size();
        accountUser.setId(UUID.randomUUID().toString());

        // Create the AccountUser
        AccountUserDTO accountUserDTO = accountUserMapper.toDto(accountUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountUserDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountUser in the database
        List<AccountUser> accountUserList = accountUserRepository.findAll();
        assertThat(accountUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchAccountUser() throws Exception {
        int databaseSizeBeforeUpdate = accountUserRepository.findAll().size();
        accountUser.setId(UUID.randomUUID().toString());

        // Create the AccountUser
        AccountUserDTO accountUserDTO = accountUserMapper.toDto(accountUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountUser in the database
        List<AccountUser> accountUserList = accountUserRepository.findAll();
        assertThat(accountUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamAccountUser() throws Exception {
        int databaseSizeBeforeUpdate = accountUserRepository.findAll().size();
        accountUser.setId(UUID.randomUUID().toString());

        // Create the AccountUser
        AccountUserDTO accountUserDTO = accountUserMapper.toDto(accountUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountUserMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountUserDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountUser in the database
        List<AccountUser> accountUserList = accountUserRepository.findAll();
        assertThat(accountUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdateAccountUserWithPatch() throws Exception {
        // Initialize the database
        accountUser.setId(UUID.randomUUID().toString());
        accountUserRepository.save(accountUser);

        int databaseSizeBeforeUpdate = accountUserRepository.findAll().size();

        // Update the accountUser using partial update
        AccountUser partialUpdatedAccountUser = new AccountUser();
        partialUpdatedAccountUser.setId(accountUser.getId());

        partialUpdatedAccountUser
            .kind(UPDATED_KIND)
            .balanceIn(UPDATED_BALANCE_IN)
            .balanceOut(UPDATED_BALANCE_OUT)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS);

        restAccountUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountUser))
            )
            .andExpect(status().isOk());

        // Validate the AccountUser in the database
        List<AccountUser> accountUserList = accountUserRepository.findAll();
        assertThat(accountUserList).hasSize(databaseSizeBeforeUpdate);
        AccountUser testAccountUser = accountUserList.get(accountUserList.size() - 1);
        assertThat(testAccountUser.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAccountUser.getKind()).isEqualTo(UPDATED_KIND);
        assertThat(testAccountUser.getBalanceIn()).isEqualByComparingTo(UPDATED_BALANCE_IN);
        assertThat(testAccountUser.getBalanceOut()).isEqualByComparingTo(UPDATED_BALANCE_OUT);
        assertThat(testAccountUser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testAccountUser.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testAccountUser.getAddress2()).isEqualTo(DEFAULT_ADDRESS_2);
    }

    @Test
    void fullUpdateAccountUserWithPatch() throws Exception {
        // Initialize the database
        accountUser.setId(UUID.randomUUID().toString());
        accountUserRepository.save(accountUser);

        int databaseSizeBeforeUpdate = accountUserRepository.findAll().size();

        // Update the accountUser using partial update
        AccountUser partialUpdatedAccountUser = new AccountUser();
        partialUpdatedAccountUser.setId(accountUser.getId());

        partialUpdatedAccountUser
            .name(UPDATED_NAME)
            .kind(UPDATED_KIND)
            .balanceIn(UPDATED_BALANCE_IN)
            .balanceOut(UPDATED_BALANCE_OUT)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .address2(UPDATED_ADDRESS_2);

        restAccountUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountUser.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountUser))
            )
            .andExpect(status().isOk());

        // Validate the AccountUser in the database
        List<AccountUser> accountUserList = accountUserRepository.findAll();
        assertThat(accountUserList).hasSize(databaseSizeBeforeUpdate);
        AccountUser testAccountUser = accountUserList.get(accountUserList.size() - 1);
        assertThat(testAccountUser.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAccountUser.getKind()).isEqualTo(UPDATED_KIND);
        assertThat(testAccountUser.getBalanceIn()).isEqualByComparingTo(UPDATED_BALANCE_IN);
        assertThat(testAccountUser.getBalanceOut()).isEqualByComparingTo(UPDATED_BALANCE_OUT);
        assertThat(testAccountUser.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testAccountUser.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testAccountUser.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
    }

    @Test
    void patchNonExistingAccountUser() throws Exception {
        int databaseSizeBeforeUpdate = accountUserRepository.findAll().size();
        accountUser.setId(UUID.randomUUID().toString());

        // Create the AccountUser
        AccountUserDTO accountUserDTO = accountUserMapper.toDto(accountUser);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountUserDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountUser in the database
        List<AccountUser> accountUserList = accountUserRepository.findAll();
        assertThat(accountUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchAccountUser() throws Exception {
        int databaseSizeBeforeUpdate = accountUserRepository.findAll().size();
        accountUser.setId(UUID.randomUUID().toString());

        // Create the AccountUser
        AccountUserDTO accountUserDTO = accountUserMapper.toDto(accountUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountUserDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountUser in the database
        List<AccountUser> accountUserList = accountUserRepository.findAll();
        assertThat(accountUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamAccountUser() throws Exception {
        int databaseSizeBeforeUpdate = accountUserRepository.findAll().size();
        accountUser.setId(UUID.randomUUID().toString());

        // Create the AccountUser
        AccountUserDTO accountUserDTO = accountUserMapper.toDto(accountUser);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountUserMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(accountUserDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountUser in the database
        List<AccountUser> accountUserList = accountUserRepository.findAll();
        assertThat(accountUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deleteAccountUser() throws Exception {
        // Initialize the database
        accountUser.setId(UUID.randomUUID().toString());
        accountUserRepository.save(accountUser);

        int databaseSizeBeforeDelete = accountUserRepository.findAll().size();

        // Delete the accountUser
        restAccountUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountUser.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AccountUser> accountUserList = accountUserRepository.findAll();
        assertThat(accountUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
