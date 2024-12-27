package com.konsol.core.service;

import com.konsol.core.domain.playstation.PlaystationDevice;
import com.konsol.core.service.api.dto.*;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link PlaystationDevice}.
 */
public interface PlaystationDeviceService {
    /**
     * Save a playstationDevice.
     *
     * @param playstationDeviceDTO the entity to save.
     * @return the persisted entity.
     */
    PsDeviceDTO save(PsDeviceDTO playstationDeviceDTO);

    /**
     * Updates a playstationDevice.
     *
     * @param playstationDeviceDTO the entity to update.
     * @return the persisted entity.
     */
    PsDeviceDTO update(PsDeviceDTO playstationDeviceDTO);

    /**
     * Partially updates a playstationDevice.
     *
     * @param playstationDeviceDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PsDeviceDTO> partialUpdate(PsDeviceDTO playstationDeviceDTO);

    /**
     * Get all the playstationDevices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PsDeviceDTO> findAll(Pageable pageable);

    List<PsDeviceDTO> findAll();

    /**
     * get all items categories in items list
     * @return list of string contains all categories distinct
     */
    List<CategoryItem> getAllItemCategories();

    List<PsDeviceDTO> findAllByCategory(String category);
    /**
     * Get the "id" playstationDevice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PsDeviceDTO> findOne(String id);

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
    PsDeviceDTO startSession(String deviceId, StartDeviceSessionDTO startDeviceSessionDTO);

    /**
     * Get active session for a device.
     *
     * @param deviceId the id of the device
     * @return the active session DTO if exists, or null
     */
    Optional<PsSessionDTO> getDeviceSession(String deviceId);

    /**
     * Stop an active session for a device.
     *
     * @param deviceId the id of the device
     * @return the stopped session DTO
     * @throws RuntimeException if device not found or no active session exists
     */
    PsDeviceDTO stopSession(String deviceId);

    PsDeviceDTO addOrderToDevice(String id, CreateInvoiceItemDTO createInvoiceItemDTO);

    PsDeviceDTO updateDeviceOrder(String id, String orderId, InvoiceItemUpdateDTO invoiceItemUpdateDTO);

    void deleteDeviceOrder(String id, String orderId);

    PsDeviceDTO moveDevice(String id, String deviceId);
}
