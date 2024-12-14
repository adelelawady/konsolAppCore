package com.konsol.core.service.impl;

import com.konsol.core.domain.Invoice;
import com.konsol.core.domain.PlayStationSession;
import com.konsol.core.domain.PlaystationDevice;
import com.konsol.core.domain.enumeration.InvoiceKind;
import com.konsol.core.repository.InvoiceRepository;
import com.konsol.core.repository.PlayStationSessionRepository;
import com.konsol.core.repository.PlaystationDeviceRepository;
import com.konsol.core.service.InvoiceService;
import com.konsol.core.service.PlaystationDeviceService;
import com.konsol.core.service.api.dto.*;
import com.konsol.core.service.dto.PlayStationSessionDTO;
import com.konsol.core.service.dto.PlaystationDeviceDTO;
import com.konsol.core.service.mapper.PlayStationSessionMapper;
import com.konsol.core.service.mapper.PlaystationDeviceMapper;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.konsol.core.domain.PlaystationDevice}.
 */
@Service
public class PlaystationDeviceServiceImpl implements PlaystationDeviceService {

    private static final Logger LOG = LoggerFactory.getLogger(PlaystationDeviceServiceImpl.class);

    private final PlaystationDeviceRepository playstationDeviceRepository;
    private final PlaystationDeviceMapper playstationDeviceMapper;
    private final PlayStationSessionRepository playStationSessionRepository;
    private final PlayStationSessionMapper playStationSessionMapper;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceService invoiceService;

    public PlaystationDeviceServiceImpl(
        PlaystationDeviceRepository playstationDeviceRepository,
        PlaystationDeviceMapper playstationDeviceMapper,
        PlayStationSessionRepository playStationSessionRepository,
        PlayStationSessionMapper playStationSessionMapper,
        InvoiceRepository invoiceRepository,
        InvoiceService invoiceService
    ) {
        this.playstationDeviceRepository = playstationDeviceRepository;
        this.playstationDeviceMapper = playstationDeviceMapper;
        this.playStationSessionRepository = playStationSessionRepository;
        this.playStationSessionMapper = playStationSessionMapper;
        this.invoiceRepository = invoiceRepository;
        this.invoiceService = invoiceService;
    }

    @Override
    public PlaystationDeviceDTO save(PlaystationDeviceDTO playstationDeviceDTO) {
        LOG.debug("Request to save PlaystationDevice : {}", playstationDeviceDTO);
        PlaystationDevice playstationDevice = playstationDeviceMapper.toEntity(playstationDeviceDTO);
        playstationDevice = playstationDeviceRepository.save(playstationDevice);
        return playstationDeviceMapper.toDto(playstationDevice);
    }

    @Override
    public PlaystationDeviceDTO update(PlaystationDeviceDTO playstationDeviceDTO) {
        LOG.debug("Request to update PlaystationDevice : {}", playstationDeviceDTO);
        PlaystationDevice playstationDevice = playstationDeviceMapper.toEntity(playstationDeviceDTO);
        playstationDevice = playstationDeviceRepository.save(playstationDevice);
        return playstationDeviceMapper.toDto(playstationDevice);
    }

    @Override
    public Optional<PlaystationDeviceDTO> partialUpdate(PlaystationDeviceDTO playstationDeviceDTO) {
        LOG.debug("Request to partially update PlaystationDevice : {}", playstationDeviceDTO);

        return playstationDeviceRepository
            .findById(playstationDeviceDTO.getId())
            .map(existingPlaystationDevice -> {
                playstationDeviceMapper.partialUpdate(existingPlaystationDevice, playstationDeviceDTO);

                return existingPlaystationDevice;
            })
            .map(playstationDeviceRepository::save)
            .map(playstationDeviceMapper::toDto);
    }

    @Override
    public Page<PlaystationDeviceDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PlaystationDevices");
        return playstationDeviceRepository.findAll(pageable).map(playstationDeviceMapper::toDto);
    }

    @Override
    public Optional<PlaystationDeviceDTO> findOne(String id) {
        LOG.debug("Request to get PlaystationDevice : {}", id);
        return playstationDeviceRepository.findById(id).map(playstationDeviceMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete PlaystationDevice : {}", id);
        playstationDeviceRepository.deleteById(id);
    }

    @Override
    public PlaystationDeviceDTO startSession(String deviceId) {
        LOG.debug("Request to start session for PlaystationDevice : {}", deviceId);

        // Find device
        PlaystationDevice device = playstationDeviceRepository
            .findById(deviceId)
            .orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId));

        // Check if device is not in use
        if (device.getActive()) {
            throw new RuntimeException("Device is already in use");
        }

        // Initialize new invoice
        Invoice invoice = invoiceService.initializeNewInvoiceDomein(InvoiceKind.SALE);

        // Create new session
        PlayStationSession session = new PlayStationSession().active(true).startTime(Instant.now()).deviceId(deviceId).invoice(invoice);

        // Update device status to active
        device.setActive(true);
        PlaystationDevice updatedDevice = playstationDeviceRepository.save(device);

        // Save session
        playStationSessionRepository.save(session);

        return playstationDeviceMapper.toDto(updatedDevice);
    }

    @Override
    public Optional<PlayStationSessionDTO> getDeviceSession(String deviceId) {
        LOG.debug("Request to get active session for PlaystationDevice : {}", deviceId);

        // Find device to verify it exists
        PlaystationDevice device = playstationDeviceRepository
            .findById(deviceId)
            .orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId));

        // If device is not active, return empty
        if (!device.getActive()) {
            return Optional.empty();
        }

        // Find active session for device
        return playStationSessionRepository.findByDeviceIdAndActiveTrue(deviceId).map(playStationSessionMapper::toDto);
    }

    @Override
    public PlayStationSessionDTO stopSession(String deviceId) {
        LOG.debug("Request to stop session for PlaystationDevice : {}", deviceId);

        // Find device
        PlaystationDevice device = playstationDeviceRepository
            .findById(deviceId)
            .orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId));

        // Check if device has active session
        if (!device.getActive()) {
            throw new RuntimeException("No active session found for device: " + deviceId);
        }

        // Find active session
        PlayStationSession session = playStationSessionRepository
            .findByDeviceIdAndActiveTrue(deviceId)
            .orElseThrow(() -> new RuntimeException("No active session found for device: " + deviceId));

        // Update session
        session.setActive(false);
        session.setEndTime(Instant.now());

        // Update invoice
        Invoice invoice = session.getInvoice();
        if (invoice != null) {
            invoiceRepository.save(invoice);
        }

        // Update device status
        device.setActive(false);
        playstationDeviceRepository.save(device);

        // Save and return updated session
        PlayStationSession updatedSession = playStationSessionRepository.save(session);
        return playStationSessionMapper.toDto(updatedSession);
    }
}
