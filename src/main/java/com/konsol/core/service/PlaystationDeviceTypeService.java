package com.konsol.core.service;

import com.konsol.core.service.dto.PlaystationDeviceTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.konsol.core.domain.PlaystationDeviceType}.
 */
public interface PlaystationDeviceTypeService {
    /**
     * Save a playstationDeviceType.
     *
     * @param playstationDeviceTypeDTO the entity to save.
     * @return the persisted entity.
     */
    PlaystationDeviceTypeDTO save(PlaystationDeviceTypeDTO playstationDeviceTypeDTO);

    /**
     * Updates a playstationDeviceType.
     *
     * @param playstationDeviceTypeDTO the entity to update.
     * @return the persisted entity.
     */
    PlaystationDeviceTypeDTO update(PlaystationDeviceTypeDTO playstationDeviceTypeDTO);

    /**
     * Partially updates a playstationDeviceType.
     *
     * @param playstationDeviceTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlaystationDeviceTypeDTO> partialUpdate(PlaystationDeviceTypeDTO playstationDeviceTypeDTO);

    /**
     * Get all the playstationDeviceTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlaystationDeviceTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" playstationDeviceType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlaystationDeviceTypeDTO> findOne(String id);

    /**
     * Delete the "id" playstationDeviceType.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
