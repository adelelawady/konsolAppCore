package com.konsol.core.service.impl;

import static com.konsol.core.repository.PlaystationDeviceRepository.DEVICES_BY_CATEGORY;
import static com.konsol.core.repository.PlaystationDeviceRepository.DEVICE_BY_DEVICE_ID;

import com.konsol.core.domain.Invoice;
import com.konsol.core.domain.Item;
import com.konsol.core.domain.enumeration.InvoiceKind;
import com.konsol.core.domain.playstation.PlayStationSession;
import com.konsol.core.domain.playstation.PlaystationDevice;
import com.konsol.core.domain.playstation.PlaystationDeviceType;
import com.konsol.core.repository.*;
import com.konsol.core.service.InvoiceService;
import com.konsol.core.service.ItemService;
import com.konsol.core.service.PlaystationDeviceService;
import com.konsol.core.service.PrintableService.PlayStationReceiptService;
import com.konsol.core.service.PrintableService.ReceiptPrinter;
import com.konsol.core.service.api.dto.*;
import com.konsol.core.service.mapper.PlayStationSessionMapper;
import com.konsol.core.service.mapper.PlaystationDeviceMapper;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCursor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import javax.print.PrintException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
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
    private final CacheManager cacheManager;
    private final ItemService itemService;
    private final MongoTemplate mongoTemplate;

    private final PlayStationReceiptService playStationReceiptService;
    private final ReceiptPrinter receiptPrinter;
    private final PlaystationContainerRepository playstationContainerRepository;

    public PlaystationDeviceServiceImpl(
        PlaystationDeviceRepository playstationDeviceRepository,
        PlaystationDeviceMapper playstationDeviceMapper,
        PlayStationSessionRepository playStationSessionRepository,
        PlayStationSessionMapper playStationSessionMapper,
        InvoiceRepository invoiceRepository,
        InvoiceService invoiceService,
        PlaystationDeviceTypeRepository playstationDeviceTypeRepository,
        CacheManager cacheManager,
        ItemService itemService,
        MongoTemplate mongoTemplate,
        PlayStationReceiptService playStationReceiptService,
        ReceiptPrinter receiptPrinter,
        PlaystationContainerRepository playstationContainerRepository,
        PlaystationContainerRepository playstationContainerRepository1
    ) {
        this.playstationDeviceRepository = playstationDeviceRepository;
        this.playstationDeviceMapper = playstationDeviceMapper;
        this.playStationSessionRepository = playStationSessionRepository;
        this.playStationSessionMapper = playStationSessionMapper;
        this.invoiceRepository = invoiceRepository;
        this.invoiceService = invoiceService;
        this.playstationDeviceTypeRepository = playstationDeviceTypeRepository;
        this.cacheManager = cacheManager;
        this.itemService = itemService;
        this.mongoTemplate = mongoTemplate;
        this.playStationReceiptService = playStationReceiptService;
        this.receiptPrinter = receiptPrinter;
        this.playstationContainerRepository = playstationContainerRepository1;
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

        Optional<PlaystationDevice> playstationDeviceOptional = playstationDeviceRepository.findById(PsDeviceDTO.getId());
        if (playstationDeviceOptional.isEmpty()) {
            return null;
        }
        PlaystationDevice playstationDevice = playstationDeviceMapper.toEntity(PsDeviceDTO);
        playstationDevice = playstationDeviceRepository.save(playstationDevice);
        playstationDevice.setActive(playstationDeviceOptional.get().getActive());
        playstationDevice = playstationDeviceRepository.save(playstationDevice);
        clearDeviceCaches(playstationDevice);
        return playstationDeviceMapper.toDto(playstationDevice);
    }

    @Override
    public Optional<PsDeviceDTO> partialUpdate(PsDeviceDTO PsDeviceDTO) {
        LOG.debug("Request to partially update PlaystationDevice : {}", PsDeviceDTO);

        return playstationDeviceRepository
            .findById(PsDeviceDTO.getId())
            .map(existingPlaystationDevice -> {
                playstationDeviceMapper.partialUpdate(existingPlaystationDevice, PsDeviceDTO);
                PsDeviceDTO.setActive(existingPlaystationDevice.getActive());
                clearDeviceCaches(existingPlaystationDevice);
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
    public List<CategoryItem> getAllItemCategories() {
        List<CategoryItem> categoryList = new ArrayList<>();
        DistinctIterable distinctIterable = mongoTemplate.getCollection("ps_device").distinct("category", String.class);
        MongoCursor cursor = distinctIterable.iterator();
        while (cursor.hasNext()) {
            String category = (String) cursor.next();
            CategoryItem categoryItem = new CategoryItem();
            categoryItem.setName(category);
            categoryList.add(categoryItem);
        }
        return categoryList;
    }

    @Override
    public List<PsDeviceDTO> findAllByCategory(String category) {
        LOG.debug("Request to get all PlaystationDevices");
        return playstationDeviceRepository
            .findAllByCategory(category)
            .stream()
            .map(playstationDeviceMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<PsDeviceDTO> findOne(String id) {
        LOG.debug("Request to get PlaystationDevice : {}", id);
        return playstationDeviceRepository.findById(id).map(playstationDeviceMapper::toDto);
    }

    @Override
    public void delete(String id) {
        // Find device
        PlaystationDevice device = playstationDeviceRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Device not found with id: " + id));
        LOG.debug("Request to delete PlaystationDevice : {}", id);
        playstationDeviceRepository.deleteById(id);

        clearDeviceCaches(device);
    }

    @Override
    public PsDeviceDTO startSession(String deviceId, StartDeviceSessionDTO startDeviceSessionDTO) {
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
            .invoice(invoice)
            .containerId(startDeviceSessionDTO.getContainerId());

        // Save session
        session = playStationSessionRepository.save(session);

        // Update device status to active
        device.setActive(true);
        device.setSession(session);
        PlaystationDevice updatedDevice = playstationDeviceRepository.save(device);
        clearDeviceCaches(updatedDevice);
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

    /**
     * Stops the active session for a PlayStation device.
     *
     * @param deviceId The unique identifier of the PlayStation device.
     * @return PsDeviceDTO containing updated device details.
     * @throws RuntimeException if the device is not found or there is no active session.
     */
    @Override
    public PsDeviceDTO stopSession(String deviceId, PlaystationEndSessionDTO playstationEndSessionDTO) {
        // Log the request to stop a session
        LOG.debug("Request to stop session for PlayStationDevice : {}", deviceId);

        // Step 1: Find the device by ID
        PlaystationDevice device = playstationDeviceRepository
            .findById(deviceId)
            .orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId));

        // Step 2: Check if the device has an active session
        if (!device.getActive()) {
            throw new RuntimeException("No active session found for device: " + deviceId);
        }

        if (device.getTimeManagement()) {
            // Step 3: Handle product association for the session
            assert device.getSession() != null;
            Item itemProduct = device.getSession().getType().getItem();

            if (itemProduct == null) {
                // Create a new Item/Product if it doesn't exist
                Item item = new Item();
                item.setName(device.getSession().getType().getName() + " - [PlayStation]");
                item.setPrice1(String.valueOf(device.getSession().getType().getPrice()));
                item.setCategory("PlayStation"); // Assign an appropriate category
                item.setCheckQty(false);
                item.setDeletable(false);
                item.setBuildIn(true);
                item.setCost(new BigDecimal(0));

                // Save the new item
                ItemDTO itemDTO = itemService.save(item);
                itemProduct = itemService.findOneById(itemDTO.getId()).get();

                // Update the session type with the created item
                device.getSession().getType().setItem(item);
                playstationDeviceTypeRepository.save(device.getSession().getType());
            }

            // Step 4: Add the current session to the invoice
            CreateInvoiceItemDTO createInvoiceItemDTO = new CreateInvoiceItemDTO();
            createInvoiceItemDTO.setQty(BigDecimal.ONE);
            createInvoiceItemDTO.setItemId(itemProduct.getId());
            createInvoiceItemDTO.setPrice(calculateSessionTimePrice(device.getSession()));

            invoiceService.addInvoiceItem(device.getSession().getInvoice().getId(), createInvoiceItemDTO);
        }
        // Step 5: Add older session items to the invoice

        if (
            device.getSession() != null &&
            device.getSession().getDeviceSessions() != null &&
            !device.getSession().getDeviceSessions().isEmpty()
        ) {
            for (PlayStationSession session : device.getSession().getDeviceSessions()) {
                CreateInvoiceItemDTO sessionInvoiceItem = new CreateInvoiceItemDTO();
                sessionInvoiceItem.setQty(BigDecimal.ONE);
                sessionInvoiceItem.setItemId(session.getType().getItem().getId());
                sessionInvoiceItem.setPrice(calculateSessionTimePrice(session));
                invoiceService.addInvoiceItem(device.getSession().getInvoice().getId(), sessionInvoiceItem);
            }
        }
        clearDeviceCaches(device);

        InvoiceDTO invoiceDTO = invoiceService.findOne(device.getSession().getInvoice().getId()).get();

        InvoiceUpdateDTO invoiceUpdateDTO = new InvoiceUpdateDTO();
        invoiceUpdateDTO.setId(invoiceDTO.getId());
        if (playstationEndSessionDTO.getMatchFinalUserPrice()) {
            invoiceUpdateDTO.setUserNetPrice(invoiceDTO.getNetPrice());
        }
        invoiceUpdateDTO.setUserNetCost(invoiceDTO.getNetCost());
        invoiceService.updateInvoice(invoiceUpdateDTO);

        // Step 6: Finalize and save the invoice
        invoiceService.saveInvoice(invoiceDTO.getId());
        PlayStationSession session;
        // Step 7: Update the active session
        try {
            session =
                playStationSessionRepository
                    .findByDeviceIdAndActiveTrue(deviceId)
                    .orElseThrow(() -> new RuntimeException("No active session found for device: " + deviceId));
        } catch (Exception e) {
            session = playStationSessionRepository.findAllByDeviceIdAndActiveTrue(deviceId).stream().findFirst().get();
            PlayStationSession finalSession = session;
            playStationSessionRepository
                .findAllByDeviceIdAndActiveTrue(deviceId)
                .forEach(playStationSession -> {
                    if (!playStationSession.getId().equals(finalSession.getId())) {
                        playStationSession.setActive(false);
                        playStationSession.setEndTime(Instant.now());
                        playStationSessionRepository.save(playStationSession);
                    }
                });
        }

        session.setActive(false);
        session.setEndTime(Instant.now());

        // Step 8: Update the device status
        device.setActive(false);
        device.setSession(null);
        device = playstationDeviceRepository.save(device);

        // Step 9: Save and return the updated session
        session = playStationSessionRepository.save(session);

        if (playstationEndSessionDTO.getPrintSessionRecipt()) {
            try {
                printReceipt(session, "Microsoft Print to PDF");
            } catch (Exception e) {
                LOG.debug("Error printing receipt", e);
            }
        }

        clearDeviceCaches(device);
        return playstationDeviceMapper.toDto(device);
    }

    @Override
    public void printReceipt(PlayStationSession session, String printerName) {
        try {
            boolean isPdfPrinter = printerName.toLowerCase().contains("pdf");

            // Prepare the receipt content
            playStationReceiptService.prepareReceipt(session);

            // Print using receipt printer service
            receiptPrinter.print(printerName, false);
        } catch (PrintException e) {
            LOG.error("Error printing receipt", e);
            throw new RuntimeException("Failed to print receipt", e);
        }
    }

    @Override
    public PsDeviceDTO addOrderToDevice(String deviceId, CreateInvoiceItemDTO createInvoiceItemDTO) {
        LOG.debug("Request to add order for PlaystationDevice : {}", deviceId);
        // Find device
        PlaystationDevice device = playstationDeviceRepository
            .findById(deviceId)
            .orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId));
        // Check if device is not in use
        if (device.getSession() == null && !device.getTimeManagement()) {
            playstationContainerRepository
                .findByCategory(device.getCategory())
                .ifPresent(playstationContainer -> {
                    StartDeviceSessionDTO startDeviceSessionDTO = new StartDeviceSessionDTO();
                    startDeviceSessionDTO.setContainerId(playstationContainer.getId());
                    this.startSession(deviceId, startDeviceSessionDTO);
                    clearAllDevicesCaches();
                });
            //  this.startSession(deviceId, new StartDeviceSessionDTO());
        } else if (device.getSession() == null && device.getTimeManagement()) {
            throw new RuntimeException("Device is Not Active");
        }
        invoiceService.addInvoiceItem(device.getSession().getInvoice().getId(), createInvoiceItemDTO);
        clearDeviceCaches(device);
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
        clearDeviceCaches(device);

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
        clearDeviceCaches(device);
        playstationDeviceRepository
            .findById(deviceId)
            .ifPresent(playstationDevice -> {
                if (
                    playstationDevice.getSession() != null &&
                    !playstationDevice.getTimeManagement() &&
                    playstationDevice.getSession().getInvoice().getInvoiceItems().isEmpty()
                ) {
                    invoiceService.delete(playstationDevice.getSession().getInvoice().getId());
                    playStationSessionRepository.deleteById(playstationDevice.getSession().getId());
                    playstationDevice.setSession(null);
                    playstationDevice.setActive(false);
                    playstationDeviceRepository.save(playstationDevice);
                    clearAllDevicesCaches();
                }
            });
        clearDeviceCaches(device);
    }

    @Override
    public PsDeviceDTO moveDevice(String id, String deviceId) {
        // Find device
        PlaystationDevice fromDevice = playstationDeviceRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId));
        // Check if device is not in use
        if (fromDevice.getSession() == null) {
            throw new RuntimeException("Device is Not Active");
        }

        // Find device
        PlaystationDevice toDevice = playstationDeviceRepository
            .findById(deviceId)
            .orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId));

        if (toDevice.getSession() != null) {
            throw new RuntimeException("Device is already Active");
        }

        if (toDevice.getType() == null) {
            throw new RuntimeException("Device Must Have Type");
        }

        if (fromDevice.getTimeManagement() && !toDevice.getTimeManagement()) {
            throw new RuntimeException("Both devices must have a Time Management as two must be same type");
        }

        if (!fromDevice.getTimeManagement() && toDevice.getTimeManagement()) {
            throw new RuntimeException("Both devices must Not have a Time Management as two must be same type");
        }
        // Find active session
        PlayStationSession session = playStationSessionRepository
            .findByDeviceIdAndActiveTrue(id)
            .orElseThrow(() -> new RuntimeException("No active session found for device: " + deviceId));
        if (fromDevice.getTimeManagement()) {
            PlayStationSession lastSession = new PlayStationSession();
            lastSession.setDevice(session.getDevice());
            lastSession.setType(session.getType());
            lastSession.setStartTime(session.getStartTime());
            lastSession.setEndTime(Instant.now());
            lastSession.setInvoice(null);
            lastSession.setDeviceSessions(null);

            session.setDeviceSessionsNetPrice(
                session.getDeviceSessionsNetPrice().add(this.calculateSessionTimePrice(fromDevice.getSession()))
            );

            session.getDeviceSessions().add(lastSession);

            session.setStartTime(Instant.now());
        } else {
            session.setDeviceSessionsNetPrice(new BigDecimal(0));
        }

        session.setType(toDevice.getType());
        session.setDevice(toDevice);
        session.setEndTime(null);
        session = playStationSessionRepository.save(session);

        fromDevice.setActive(false);
        fromDevice.setSession(null);
        playstationDeviceRepository.save(fromDevice);
        toDevice.setSession(session);
        toDevice.setActive(true);

        playstationDeviceRepository.save(toDevice);
        clearDeviceCaches(toDevice);
        clearDeviceCaches(fromDevice);
        return playstationDeviceMapper.toDto(playstationDeviceRepository.save(fromDevice));
    }

    @Override
    //   @CacheEvict(cacheNames = DEVICES_BY_CATEGORY, allEntries = true)
    public void clearDeviceCaches(PlaystationDevice device) {
        LOG.debug("Clearing cache for device category: {}", device.getCategory());
        Objects.requireNonNull(cacheManager.getCache(DEVICES_BY_CATEGORY)).evict(device.getCategory());

        Objects.requireNonNull(cacheManager.getCache(DEVICE_BY_DEVICE_ID)).evict(device.getId());
    }

    @Override
    public void clearAllDevicesCaches() {
        LOG.debug("Clearing all device caches");
        Objects.requireNonNull(cacheManager.getCache(DEVICES_BY_CATEGORY)).clear();
        Objects.requireNonNull(cacheManager.getCache(DEVICE_BY_DEVICE_ID)).clear();
    }

    @Override
    public PsDeviceDTO updateSessionInvoice(String deviceId, InvoiceUpdateDTO invoiceUpdateDTO) {
        // Find device
        PlaystationDevice device = playstationDeviceRepository
            .findById(deviceId)
            .orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId));
        // Check if device is not in use
        if (device.getSession() == null) {
            throw new RuntimeException("Device is Not Active");
        }

        invoiceService.updateInvoice(invoiceUpdateDTO);

        clearDeviceCaches(device);

        return playstationDeviceMapper.toDto(
            playstationDeviceRepository.findById(deviceId).orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId))
        );
    }

    private BigDecimal calculateSessionTimePrice(PlayStationSession session) {
        if (session == null || session.getStartTime() == null || session.getType() == null) {
            return BigDecimal.ZERO;
        }

        // Calculate time difference in milliseconds
        long startTime = Date.from(session.getStartTime()).getTime();
        long now = System.currentTimeMillis();
        if (session.getEndTime() != null) {
            now = Date.from(session.getEndTime()).getTime();
        }
        long durationInMs = now - startTime;

        // Convert to hours
        double durationInHours = durationInMs / (1000.0 * 60.0 * 60.0);

        // Get hourly rate
        BigDecimal hourlyRate = session.getType().getPrice();

        // Calculate total cost and round to whole number
        return hourlyRate.multiply(BigDecimal.valueOf(durationInHours)).setScale(0, RoundingMode.HALF_UP);
    }

    @Override
    public PsDeviceDTO changeDeviceType(String deviceId, String typeId, boolean updateSession) {
        LOG.debug("Request to update device type for device: {} to type: {}", deviceId, typeId);

        // Find device
        PlaystationDevice device = playstationDeviceRepository
            .findById(deviceId)
            .orElseThrow(() -> new RuntimeException("Device not found with id: " + deviceId));

        // Find new device type
        PlaystationDeviceType newType = playstationDeviceTypeRepository
            .findById(typeId)
            .orElseThrow(() -> new RuntimeException("Device type not found with id: " + typeId));

        // Update device type
        device.setType(newType);

        // If updateSession is true and there's an active session, update its type
        if (updateSession && device.getActive() && device.getSession() != null) {
            PlayStationSession session = device.getSession();

            if (device.getTimeManagement()) {
                // If time management is enabled, create a new session entry for the old type
                PlayStationSession lastSession = new PlayStationSession();
                lastSession.setDevice(session.getDevice());
                lastSession.setType(session.getType());
                lastSession.setStartTime(session.getStartTime());
                lastSession.setEndTime(Instant.now());
                lastSession.setInvoice(null);
                lastSession.setDeviceSessions(null);

                // Add the price for the time used with old type
                session.setDeviceSessionsNetPrice(session.getDeviceSessionsNetPrice().add(calculateSessionTimePrice(session)));

                // Add the old session to device sessions history
                if (session.getDeviceSessions() == null) {
                    session.setDeviceSessions(new ArrayList<>());
                }
                session.getDeviceSessions().add(lastSession);
            }

            // Update session with new type and reset start time
            session.setType(newType);
            session.setStartTime(Instant.now());
            session = playStationSessionRepository.save(session);
            device.setSession(session);
        }

        // Save and return updated device
        device = playstationDeviceRepository.save(device);
        clearDeviceCaches(device);

        return playstationDeviceMapper.toDto(device);
    }
}
