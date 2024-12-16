package com.konsol.core.service.impl;

import com.konsol.core.domain.Invoice;
import com.konsol.core.domain.Item;
import com.konsol.core.domain.enumeration.InvoiceKind;
import com.konsol.core.domain.playstation.PlayStationSession;
import com.konsol.core.domain.playstation.PlaystationDevice;
import com.konsol.core.repository.InvoiceRepository;
import com.konsol.core.repository.PlayStationSessionRepository;
import com.konsol.core.repository.PlaystationDeviceRepository;
import com.konsol.core.repository.PlaystationDeviceTypeRepository;
import com.konsol.core.service.InvoiceService;
import com.konsol.core.service.ItemService;
import com.konsol.core.service.PlaystationDeviceService;
import com.konsol.core.service.api.dto.*;
import com.konsol.core.service.mapper.PlayStationSessionMapper;
import com.konsol.core.service.mapper.PlaystationDeviceMapper;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
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
    private final PlaystationDeviceTypeRepository playstationDeviceTypeRepository;

    private final ItemService itemService;

    public PlaystationDeviceServiceImpl(
        PlaystationDeviceRepository playstationDeviceRepository,
        PlaystationDeviceMapper playstationDeviceMapper,
        PlayStationSessionRepository playStationSessionRepository,
        PlayStationSessionMapper playStationSessionMapper,
        InvoiceRepository invoiceRepository,
        InvoiceService invoiceService,
        PlaystationDeviceTypeRepository playstationDeviceTypeRepository,
        ItemService itemService
    ) {
        this.playstationDeviceRepository = playstationDeviceRepository;
        this.playstationDeviceMapper = playstationDeviceMapper;
        this.playStationSessionRepository = playStationSessionRepository;
        this.playStationSessionMapper = playStationSessionMapper;
        this.invoiceRepository = invoiceRepository;
        this.invoiceService = invoiceService;
        this.playstationDeviceTypeRepository = playstationDeviceTypeRepository;
        this.itemService = itemService;
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
        if (device.getActive() && device.getSession() != null) {
            throw new RuntimeException("Device is already in use");
        }

        if (device.getType() == null) {
            throw new RuntimeException("Device Must Have Type");
        }

        // Initialize new invoice
        Invoice invoice = invoiceService.initializeNewInvoiceDomein(InvoiceKind.SALE);

        // Create new session
        PlayStationSession session = new PlayStationSession()
            .type(device.getType())
            .active(true)
            .startTime(Instant.now())
            .device(device)
            .invoice(invoice);

        // Save session
        session = playStationSessionRepository.save(session);

        // Update device status to active
        device.setActive(true);
        device.setSession(session);
        PlaystationDevice updatedDevice = playstationDeviceRepository.save(device);

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
    public PsDeviceDTO stopSession(String deviceId) {
        LOG.debug("Request to stop session for PlaystationDevice : {}", deviceId);

        // Find device
        PlaystationDevice device = playstationDeviceRepository
            .findById(deviceId)
            .orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId));

        // Check if device has active session
        if (!device.getActive()) {
            throw new RuntimeException("No active session found for device: " + deviceId);
        }

        Item itemProduct = device.getSession().getType().getItem();

        if (itemProduct == null) {
            // Create corresponding Item/Product
            Item item = new Item();
            item.setName(device.getSession().getType().getName() + " - [PlayStation]");
            item.setPrice1(String.valueOf(device.getSession().getType().getPrice()));
            item.setCategory("PlayStation"); // Or appropriate category
            item.setCheckQty(false);
            item.setDeletable(false);
            item.setCost(new BigDecimal(0));
            // Save the item
            ItemDTO itemDTO = itemService.save(item);
            itemProduct = itemService.findOneById(itemDTO.getId()).get();
            device.getSession().getType().setItem(item);
            playstationDeviceTypeRepository.save(device.getSession().getType());
        }
        CreateInvoiceItemDTO createInvoiceItemDTO = new CreateInvoiceItemDTO();
        createInvoiceItemDTO.setQty(new BigDecimal(1));
        createInvoiceItemDTO.setItemId(itemProduct.getId());
        createInvoiceItemDTO.setPrice(calculateSessionTimePrice(device.getSession()));

        invoiceService.addInvoiceItem(device.getSession().getInvoice().getId(), createInvoiceItemDTO);

        invoiceService.saveInvoice(device.getSession().getInvoice().getId());
        // Find active session
        PlayStationSession session = playStationSessionRepository
            .findByDeviceIdAndActiveTrue(deviceId)
            .orElseThrow(() -> new RuntimeException("No active session found for device: " + deviceId));
        // Update session
        session.setActive(false);
        session.setEndTime(Instant.now());

        // Update device status
        device.setActive(false);
        device.setSession(null);
        device = playstationDeviceRepository.save(device);
        // Save and return updated session
        PlayStationSession updatedSession = playStationSessionRepository.save(session);
        return playstationDeviceMapper.toDto(device);
    }

    @Override
    public PsDeviceDTO addOrderToDevice(String deviceId, CreateInvoiceItemDTO createInvoiceItemDTO) {
        LOG.debug("Request to add order for PlaystationDevice : {}", deviceId);
        // Find device
        PlaystationDevice device = playstationDeviceRepository
            .findById(deviceId)
            .orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId));
        // Check if device is not in use
        if (device.getSession() == null) {
            throw new RuntimeException("Device is Not Active");
        }
        invoiceService.addInvoiceItem(device.getSession().getInvoice().getId(), createInvoiceItemDTO);
        return playstationDeviceMapper.toDto(
            playstationDeviceRepository.findById(deviceId).orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId))
        );
    }

    @Override
    public PsDeviceDTO updateDeviceOrder(String deviceId, String orderId, InvoiceItemUpdateDTO invoiceItemUpdateDTO) {
        LOG.debug("Request to update order for PlaystationDevice : {}", deviceId);
        // Find device
        PlaystationDevice device = playstationDeviceRepository
            .findById(deviceId)
            .orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId));
        // Check if device is not in use
        if (device.getSession() == null) {
            throw new RuntimeException("Device is Not Active");
        }
        invoiceService.updateInvoiceItem(orderId, invoiceItemUpdateDTO);
        return playstationDeviceMapper.toDto(
            playstationDeviceRepository.findById(deviceId).orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId))
        );
    }

    @Override
    public void deleteDeviceOrder(String deviceId, String orderId) {
        LOG.debug("Request to update order for PlaystationDevice : {}", deviceId);
        // Find device
        PlaystationDevice device = playstationDeviceRepository
            .findById(deviceId)
            .orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId));
        // Check if device is not in use
        if (device.getSession() == null) {
            throw new RuntimeException("Device is Not Active");
        }
        invoiceService.deleteInvoiceItem(orderId);
    }

    private BigDecimal calculateSessionTimePrice(PlayStationSession session) {
        Instant startTime = session.getStartTime();
        Instant now = Instant.now();
        // Calculate the duration in hours between the two instants
        long durationInSeconds = Duration.between(startTime, now).getSeconds();
        BigDecimal durationInHours = new BigDecimal(durationInSeconds).divide(new BigDecimal(3600), RoundingMode.HALF_UP); // 3600 seconds in an hour
        BigDecimal hourlyRate = session.getType().getPrice();
        BigDecimal cost = hourlyRate.multiply(durationInHours);
        return cost.setScale(0, RoundingMode.HALF_UP); // Rounds to the nearest whole number
    }
}
