package com.konsol.core.service;

import com.konsol.core.domain.playstation.PlaystationDeviceType;
import com.konsol.core.service.api.dto.PsDeviceType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link PlaystationDeviceType}.
 */
public interface PlaystationDeviceTypeService {
    /**
     * Save a playstationDeviceType.
     *
     * @param PsDeviceType the entity to save.
     * @return the persisted entity.
     */
    PsDeviceType save(PsDeviceType PsDeviceType);

    /**
     * Updates a playstationDeviceType.
     *
     * @param PsDeviceType the entity to update.
     * @return the persisted entity.
     */
    PsDeviceType update(PsDeviceType PsDeviceType);

    /**
     * Partially updates a playstationDeviceType.
     *
     * @param PsDeviceType the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PsDeviceType> partialUpdate(PsDeviceType PsDeviceType);

    /**
     * Get all the playstationDeviceTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PsDeviceType> findAll(Pageable pageable);

    List<PsDeviceType> findAll();
    /**
     * Get the "id" playstationDeviceType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PsDeviceType> findOne(String id);

    /**
     * Delete the "id" playstationDeviceType.
     *
     * @param id the id of the entity.
     */
    void delete(String id);
}
