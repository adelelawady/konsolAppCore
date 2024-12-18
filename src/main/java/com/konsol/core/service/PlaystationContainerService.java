package com.konsol.core.service;

import com.konsol.core.service.dto.PlaystationContainerDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.konsol.core.domain.PlaystationContainer}.
 */
public interface PlaystationContainerService {
    /**
     * Save a playstationContainer.
     *
     * @param playstationContainerDTO the entity to save.
     * @return the persisted entity.
     */
    PlaystationContainerDTO save(PlaystationContainerDTO playstationContainerDTO);

    /**
     * Updates a playstationContainer.
     *
     * @param playstationContainerDTO the entity to update.
     * @return the persisted entity.
     */
    PlaystationContainerDTO update(PlaystationContainerDTO playstationContainerDTO);

    /**
     * Partially updates a playstationContainer.
     *
     * @param playstationContainerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlaystationContainerDTO> partialUpdate(PlaystationContainerDTO playstationContainerDTO);

    /**
     * Get all the playstationContainers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlaystationContainerDTO> findAll(Pageable pageable);

    /**
     * Get the "id" playstationContainer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlaystationContainerDTO> findOne(String id);

    /**
     * Delete the "id" playstationContainer.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
