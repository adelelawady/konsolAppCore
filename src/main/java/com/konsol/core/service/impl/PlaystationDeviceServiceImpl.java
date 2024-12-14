package com.konsol.core.service.impl;

import com.konsol.core.domain.Invoice;
import com.konsol.core.domain.enumeration.InvoiceKind;
import com.konsol.core.domain.playstation.PlayStationSession;
import com.konsol.core.domain.playstation.PlaystationDevice;
import com.konsol.core.repository.InvoiceRepository;
import com.konsol.core.repository.PlayStationSessionRepository;
import com.konsol.core.repository.PlaystationDeviceRepository;
import com.konsol.core.service.InvoiceService;
import com.konsol.core.service.PlaystationDeviceService;
import com.konsol.core.service.api.dto.PsDeviceDTO;
import com.konsol.core.service.api.dto.PsSessionDTO;
import com.konsol.core.service.mapper.PlayStationSessionMapper;
import com.konsol.core.service.mapper.PlaystationDeviceMapper;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.konsol.core.domain.playstation.PlaystationDevice}.
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
    public PsDeviceDTO save(PsDeviceDTO PsDeviceDTO) {
        LOG.debug("Request to save PlaystationDevice : {}", PsDeviceDTO);
        PlaystationDevice playstationDevice = playstationDeviceMapper.toEntity(PsDeviceDTO);
        playstationDevice = playstationDeviceRepository.save(playstationDevice);
        return playstationDeviceMapper.toDto(playstationDevice);
    }

    @Override
    public PsDeviceDTO update(PsDeviceDTO PsDeviceDTO) {
        LOG.debug("Request to update PlaystationDevice : {}", PsDeviceDTO);
        PlaystationDevice playstationDevice = playstationDeviceMapper.toEntity(PsDeviceDTO);
        playstationDevice = playstationDeviceRepository.save(playstationDevice);
        return playstationDeviceMapper.toDto(playstationDevice);
    }

    @Override
    public Optional<PsDeviceDTO> partialUpdate(PsDeviceDTO PsDeviceDTO) {
        LOG.debug("Request to partially update PlaystationDevice : {}", PsDeviceDTO);

        return playstationDeviceRepository
            .findById(PsDeviceDTO.getId())
            .map(existingPlaystationDevice -> {
                playstationDeviceMapper.partialUpdate(existingPlaystationDevice, PsDeviceDTO);

                return existingPlaystationDevice;
            })
            .map(playstationDeviceRepository::save)
            .map(playstationDeviceMapper::toDto);
    }

    @Override
    public Page<PsDeviceDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PlaystationDevices");
        return playstationDeviceRepository.findAll(pageable).map(playstationDeviceMapper::toDto);
    }

    @Override
    public List<PsDeviceDTO> findAll() {
        LOG.debug("Request to get all PlaystationDevices");
        return playstationDeviceRepository.findAll().stream().map(playstationDeviceMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<PsDeviceDTO> findOne(String id) {
        LOG.debug("Request to get PlaystationDevice : {}", id);
        return playstationDeviceRepository.findById(id).map(playstationDeviceMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete PlaystationDevice : {}", id);
        playstationDeviceRepository.deleteById(id);
    }

    @Override
    public PsDeviceDTO startSession(String deviceId) {
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
        PlayStationSession session = new PlayStationSession().active(true).startTime(Instant.now()).device(device).invoice(invoice);

        // Update device status to active
        device.setActive(true);
        PlaystationDevice updatedDevice = playstationDeviceRepository.save(device);

        // Save session
        playStationSessionRepository.save(session);

        return playstationDeviceMapper.toDto(updatedDevice);
    }

    @Override
    public Optional<PsSessionDTO> getDeviceSession(String deviceId) {
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
    public PsSessionDTO stopSession(String deviceId) {
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
