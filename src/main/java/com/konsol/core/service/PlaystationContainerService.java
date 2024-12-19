package com.konsol.core.service;

import com.konsol.core.service.api.dto.PlaystationContainer;
import java.util.List;
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
     * @param PlaystationContainer the entity to save.
     * @return the persisted entity.
     */
    PlaystationContainer save(PlaystationContainer PlaystationContainer);

    /**
     * Updates a playstationContainer.
     *
     * @param PlaystationContainer the entity to update.
     * @return the persisted entity.
     */
    PlaystationContainer update(PlaystationContainer PlaystationContainer);

    /**
     * Partially updates a playstationContainer.
     *
     * @param PlaystationContainer the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlaystationContainer> partialUpdate(PlaystationContainer PlaystationContainer);

    /**
     * Get all the playstationContainers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlaystationContainer> findAll(Pageable pageable);

    List<PlaystationContainer> findAll();

    /**
     * Get the "id" playstationContainer.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlaystationContainer> findOne(String id);

    /**
     * Delete the "id" playstationContainer.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
