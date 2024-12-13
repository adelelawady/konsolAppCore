package com.konsol.core.service;

import com.konsol.core.service.dto.PlaystationDeviceDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.konsol.core.domain.PlaystationDevice}.
 */
public interface PlaystationDeviceService {
    /**
     * Save a playstationDevice.
     *
     * @param playstationDeviceDTO the entity to save.
     * @return the persisted entity.
     */
    PlaystationDeviceDTO save(PlaystationDeviceDTO playstationDeviceDTO);

    /**
     * Updates a playstationDevice.
     *
     * @param playstationDeviceDTO the entity to update.
     * @return the persisted entity.
     */
    PlaystationDeviceDTO update(PlaystationDeviceDTO playstationDeviceDTO);

    /**
     * Partially updates a playstationDevice.
     *
     * @param playstationDeviceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PlaystationDeviceDTO> partialUpdate(PlaystationDeviceDTO playstationDeviceDTO);

    /**
     * Get all the playstationDevices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlaystationDeviceDTO> findAll(Pageable pageable);

    /**
     * Get the "id" playstationDevice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlaystationDeviceDTO> findOne(String id);

    /**
     * Delete the "id" playstationDevice.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
