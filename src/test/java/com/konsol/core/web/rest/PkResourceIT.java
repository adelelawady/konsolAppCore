package com.konsol.core.web.rest;

import static com.konsol.core.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.konsol.core.IntegrationTest;
import com.konsol.core.domain.Pk;
import com.konsol.core.domain.enumeration.PkKind;
import com.konsol.core.repository.PkRepository;
import com.konsol.core.service.dto.PkDTO;
import com.konsol.core.service.mapper.PkMapper;
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
 * Integration tests for the {@link PkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PkResourceIT {

    private static final PkKind DEFAULT_KIND = PkKind.INVOICE;
    private static final PkKind UPDATED_KIND = PkKind.InvoicesItems;

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(0);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(1);

    private static final String ENTITY_API_URL = "/api/pks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private PkRepository pkRepository;

    @Autowired
    private PkMapper pkMapper;

    @Autowired
    private MockMvc restPkMockMvc;

    private Pk pk;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pk createEntity() {
        Pk pk = new Pk().kind(DEFAULT_KIND).value(DEFAULT_VALUE);
        return pk;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pk createUpdatedEntity() {
        Pk pk = new Pk().kind(UPDATED_KIND).value(UPDATED_VALUE);
        return pk;
    }

    @BeforeEach
    public void initTest() {
        pkRepository.deleteAll();
        pk = createEntity();
    }

    @Test
    void createPk() throws Exception {
        int databaseSizeBeforeCreate = pkRepository.findAll().size();
        // Create the Pk
        PkDTO pkDTO = pkMapper.toDto(pk);
        restPkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pkDTO)))
            .andExpect(status().isCreated());

        // Validate the Pk in the database
        List<Pk> pkList = pkRepository.findAll();
        assertThat(pkList).hasSize(databaseSizeBeforeCreate + 1);
        Pk testPk = pkList.get(pkList.size() - 1);
        assertThat(testPk.getKind()).isEqualTo(DEFAULT_KIND);
        assertThat(testPk.getValue()).isEqualByComparingTo(DEFAULT_VALUE);
    }

    @Test
    void createPkWithExistingId() throws Exception {
        // Create the Pk with an existing ID
        pk.setId("existing_id");
        PkDTO pkDTO = pkMapper.toDto(pk);

        int databaseSizeBeforeCreate = pkRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pkDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pk in the database
        List<Pk> pkList = pkRepository.findAll();
        assertThat(pkList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    void getAllPks() throws Exception {
        // Initialize the database
        pk.setId(UUID.randomUUID().toString());
        pkRepository.save(pk);

        // Get all the pkList
        restPkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pk.getId())))
            .andExpect(jsonPath("$.[*].kind").value(hasItem(DEFAULT_KIND.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(sameNumber(DEFAULT_VALUE))));
    }

    @Test
    void getPk() throws Exception {
        // Initialize the database
        pk.setId(UUID.randomUUID().toString());
        pkRepository.save(pk);

        // Get the pk
        restPkMockMvc
            .perform(get(ENTITY_API_URL_ID, pk.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pk.getId()))
            .andExpect(jsonPath("$.kind").value(DEFAULT_KIND.toString()))
            .andExpect(jsonPath("$.value").value(sameNumber(DEFAULT_VALUE)));
    }

    @Test
    void getNonExistingPk() throws Exception {
        // Get the pk
        restPkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    void putExistingPk() throws Exception {
        // Initialize the database
        pk.setId(UUID.randomUUID().toString());
        pkRepository.save(pk);

        int databaseSizeBeforeUpdate = pkRepository.findAll().size();

        // Update the pk
        Pk updatedPk = pkRepository.findById(pk.getId()).get();
        updatedPk.kind(UPDATED_KIND).value(UPDATED_VALUE);
        PkDTO pkDTO = pkMapper.toDto(updatedPk);

        restPkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pkDTO))
            )
            .andExpect(status().isOk());

        // Validate the Pk in the database
        List<Pk> pkList = pkRepository.findAll();
        assertThat(pkList).hasSize(databaseSizeBeforeUpdate);
        Pk testPk = pkList.get(pkList.size() - 1);
        assertThat(testPk.getKind()).isEqualTo(UPDATED_KIND);
        assertThat(testPk.getValue()).isEqualByComparingTo(UPDATED_VALUE);
    }

    @Test
    void putNonExistingPk() throws Exception {
        int databaseSizeBeforeUpdate = pkRepository.findAll().size();
        pk.setId(UUID.randomUUID().toString());

        // Create the Pk
        PkDTO pkDTO = pkMapper.toDto(pk);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pk in the database
        List<Pk> pkList = pkRepository.findAll();
        assertThat(pkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithIdMismatchPk() throws Exception {
        int databaseSizeBeforeUpdate = pkRepository.findAll().size();
        pk.setId(UUID.randomUUID().toString());

        // Create the Pk
        PkDTO pkDTO = pkMapper.toDto(pk);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pk in the database
        List<Pk> pkList = pkRepository.findAll();
        assertThat(pkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void putWithMissingIdPathParamPk() throws Exception {
        int databaseSizeBeforeUpdate = pkRepository.findAll().size();
        pk.setId(UUID.randomUUID().toString());

        // Create the Pk
        PkDTO pkDTO = pkMapper.toDto(pk);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pk in the database
        List<Pk> pkList = pkRepository.findAll();
        assertThat(pkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void partialUpdatePkWithPatch() throws Exception {
        // Initialize the database
        pk.setId(UUID.randomUUID().toString());
        pkRepository.save(pk);

        int databaseSizeBeforeUpdate = pkRepository.findAll().size();

        // Update the pk using partial update
        Pk partialUpdatedPk = new Pk();
        partialUpdatedPk.setId(pk.getId());

        partialUpdatedPk.kind(UPDATED_KIND);

        restPkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPk.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPk))
            )
            .andExpect(status().isOk());

        // Validate the Pk in the database
        List<Pk> pkList = pkRepository.findAll();
        assertThat(pkList).hasSize(databaseSizeBeforeUpdate);
        Pk testPk = pkList.get(pkList.size() - 1);
        assertThat(testPk.getKind()).isEqualTo(UPDATED_KIND);
        assertThat(testPk.getValue()).isEqualByComparingTo(DEFAULT_VALUE);
    }

    @Test
    void fullUpdatePkWithPatch() throws Exception {
        // Initialize the database
        pk.setId(UUID.randomUUID().toString());
        pkRepository.save(pk);

        int databaseSizeBeforeUpdate = pkRepository.findAll().size();

        // Update the pk using partial update
        Pk partialUpdatedPk = new Pk();
        partialUpdatedPk.setId(pk.getId());

        partialUpdatedPk.kind(UPDATED_KIND).value(UPDATED_VALUE);

        restPkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPk.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPk))
            )
            .andExpect(status().isOk());

        // Validate the Pk in the database
        List<Pk> pkList = pkRepository.findAll();
        assertThat(pkList).hasSize(databaseSizeBeforeUpdate);
        Pk testPk = pkList.get(pkList.size() - 1);
        assertThat(testPk.getKind()).isEqualTo(UPDATED_KIND);
        assertThat(testPk.getValue()).isEqualByComparingTo(UPDATED_VALUE);
    }

    @Test
    void patchNonExistingPk() throws Exception {
        int databaseSizeBeforeUpdate = pkRepository.findAll().size();
        pk.setId(UUID.randomUUID().toString());

        // Create the Pk
        PkDTO pkDTO = pkMapper.toDto(pk);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pkDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pk in the database
        List<Pk> pkList = pkRepository.findAll();
        assertThat(pkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithIdMismatchPk() throws Exception {
        int databaseSizeBeforeUpdate = pkRepository.findAll().size();
        pk.setId(UUID.randomUUID().toString());

        // Create the Pk
        PkDTO pkDTO = pkMapper.toDto(pk);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, UUID.randomUUID().toString())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pk in the database
        List<Pk> pkList = pkRepository.findAll();
        assertThat(pkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void patchWithMissingIdPathParamPk() throws Exception {
        int databaseSizeBeforeUpdate = pkRepository.findAll().size();
        pk.setId(UUID.randomUUID().toString());

        // Create the Pk
        PkDTO pkDTO = pkMapper.toDto(pk);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPkMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(pkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pk in the database
        List<Pk> pkList = pkRepository.findAll();
        assertThat(pkList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    void deletePk() throws Exception {
        // Initialize the database
        pk.setId(UUID.randomUUID().toString());
        pkRepository.save(pk);

        int databaseSizeBeforeDelete = pkRepository.findAll().size();

        // Delete the pk
        restPkMockMvc.perform(delete(ENTITY_API_URL_ID, pk.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Pk> pkList = pkRepository.findAll();
        assertThat(pkList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
