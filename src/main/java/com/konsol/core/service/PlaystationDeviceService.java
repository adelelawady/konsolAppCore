package com.konsol.core.service;

import com.konsol.core.service.dto.PlaystationDeviceDTO;
import com.konsol.core.service.dto.PlayStationSessionDTO;
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

    /**
     * Start a new session for a device.
     *
     * @param deviceId the id of the device to start session for
     * @return the created session DTO
     */
    PlaystationDeviceDTO startSession(String deviceId);

    /**
     * Get active session for a device.
     *
     * @param deviceId the id of the device
     * @return the active session DTO if exists, or null
     */
    Optional<PlayStationSessionDTO> getDeviceSession(String deviceId);

    /**
     * Stop an active session for a device.
     *
     * @param deviceId the id of the device
     * @return the stopped session DTO
     * @throws RuntimeException if device not found or no active session exists
     */
    PlayStationSessionDTO stopSession(String deviceId);
}
