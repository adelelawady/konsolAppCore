package com.konsol.core.service;

import com.konsol.core.service.dto.PriceOptionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.konsol.core.domain.PriceOption}.
 */
public interface PriceOptionService {
    /**
     * Save a priceOption.
     *
     * @param priceOptionDTO the entity to save.
     * @return the persisted entity.
     */
    PriceOptionDTO save(PriceOptionDTO priceOptionDTO);

    /**
     * Updates a priceOption.
     *
     * @param priceOptionDTO the entity to update.
     * @return the persisted entity.
     */
    PriceOptionDTO update(PriceOptionDTO priceOptionDTO);

    /**
     * Partially updates a priceOption.
     *
     * @param priceOptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PriceOptionDTO> partialUpdate(PriceOptionDTO priceOptionDTO);

    /**
     * Get all the priceOptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PriceOptionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" priceOption.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PriceOptionDTO> findOne(String id);

    /**
     * Delete the "id" priceOption.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
