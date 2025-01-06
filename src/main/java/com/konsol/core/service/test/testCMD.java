package com.konsol.core.service.test;

import static org.reflections.Reflections.log;

import com.konsol.core.domain.*;
import com.konsol.core.domain.PlaystationContainer;
import com.konsol.core.domain.VAR.*;
import com.konsol.core.domain.VAR.Record;
import com.konsol.core.domain.enumeration.InvoiceKind;
import com.konsol.core.domain.enumeration.PkKind;
import com.konsol.core.domain.playstation.PlayStationSession;
import com.konsol.core.domain.playstation.PlaystationDevice;
import com.konsol.core.domain.playstation.PlaystationDeviceType;
import com.konsol.core.repository.*;
import com.konsol.core.repository.VAR.ProductRepository;
import com.konsol.core.service.*;
import com.konsol.core.service.api.dto.*;
import com.konsol.core.service.impl.InvoiceServiceImpl;
import com.konsol.core.service.impl.PlayStationSessionServiceImpl;
import com.konsol.core.service.impl.PlaystationDeviceServiceImpl;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class testCMD implements CommandLineRunner {

    private final MongoTemplate erMongoTemplate;

    private final MongoTemplate mongoTemplate;

    private final ItemRepository itemsRepository;

    private final PlaystationContainerRepository playstationContainerRepository;

    private final PlaystationDeviceTypeService playstationDeviceTypeService;

    private final PlaystationDeviceTypeRepository playstationDeviceTypeRepository;

    private final PlaystationDeviceRepository playstationDeviceRepository;
    private final PlaystationDeviceService playstationDeviceService;

    private final PkService pkService;
    private final ItemService itemService;
    private final InvoiceService invoiceServiceImpl;
    private final PlayStationSessionRepository playStationSessionRepository;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final PlayStationSessionServiceImpl playStationSessionServiceImpl;

    public testCMD(
        @Qualifier("erMongoTemplate") MongoTemplate erMongoTemplate,
        MongoTemplate mongoTemplate,
        ItemRepository itemsRepository,
        PlaystationContainerRepository playstationContainerRepository,
        PlaystationDeviceTypeService playstationDeviceTypeService,
        com.konsol.core.repository.PlaystationDeviceTypeRepository playstationDeviceTypeRepository,
        PlaystationDeviceRepository playstationDeviceRepository,
        PlaystationDeviceService playstationDeviceServiceImpl, PkService pkService,
        ItemService itemService,
        InvoiceService invoiceServiceImpl,
        PlayStationSessionRepository playStationSessionRepository,
        InvoiceRepository invoiceRepository,
        InvoiceItemRepository invoiceItemRepository,
        PlayStationSessionServiceImpl playStationSessionServiceImpl
    ) {
        this.erMongoTemplate = erMongoTemplate;

        this.mongoTemplate = mongoTemplate;
        this.itemsRepository = itemsRepository;
        this.playstationContainerRepository = playstationContainerRepository;
        this.playstationDeviceTypeService = playstationDeviceTypeService;
        this.playstationDeviceTypeRepository = playstationDeviceTypeRepository;
        this.playstationDeviceRepository = playstationDeviceRepository;
        this.playstationDeviceService = playstationDeviceServiceImpl;
        this.pkService = pkService;
        this.itemService = itemService;
        this.invoiceServiceImpl = invoiceServiceImpl;
        this.playStationSessionRepository = playStationSessionRepository;
        this.invoiceRepository = invoiceRepository;
        this.invoiceItemRepository = invoiceItemRepository;
        this.playStationSessionServiceImpl = playStationSessionServiceImpl;
    }

    /**
     * @param args incoming main method arguments
     * @throws Exception
     */
    int recordsRetCount = 10000;

    @Override
    public void run(String... args) throws Exception {
        /*
        playstationContainerRepository.deleteAll();
        // Create and save PlayStation containers
        PlaystationContainer PlayStation = new PlaystationContainer()
            .name("PlayStation")
            .category("PlayStation")
            .defaultIcon("ps4-icon")
            .hasTimeManagement(true)
            .showType(true)
            .showTime(true)
            .canMoveDevice(true)
            .canHaveMultiTimeManagement(true)
            .orderSelectedPriceCategory("PlayStation");
            //.acceptedOrderCategories(Set.of("PS4", "PS4_PRO"));

        PlaystationContainer Tables = new PlaystationContainer()
            .name("Tables")
            .category("Tables")
            .defaultIcon("ps4pro-icon")
            .hasTimeManagement(false)
            .showType(false)
            .showTime(false)
            .canMoveDevice(true)
            .canHaveMultiTimeManagement(false)
            .orderSelectedPriceCategory("Tables");
            //.acceptedOrderCategories(Set.of("PS4", "PS4_PRO"));

        PlaystationContainer Shops = new PlaystationContainer()
            .name("Shops")
            .category("Shops")
            .defaultIcon("ps5d-icon")
            .hasTimeManagement(false)
            .showType(false)
            .showTime(false)
            .canMoveDevice(true)
            .canHaveMultiTimeManagement(false)
            .orderSelectedPriceCategory("Shops");
            //.acceptedOrderCategories(Set.of("PS5"));

        PlaystationContainer TakeAway = new PlaystationContainer()
            .name("TakeAway")
            .category("TakeAway")
            .defaultIcon("ps5-icon")
            .hasTimeManagement(false)
            .showType(false)
            .showTime(false)
            .canMoveDevice(true)
            .canHaveMultiTimeManagement(false)
            .orderSelectedPriceCategory("TakeAway");
            //.acceptedOrderCategories(Set.of("PS5"));

        // Save all containers using mongoTemplate
        mongoTemplate.save(PlayStation);
        mongoTemplate.save(Tables);
        mongoTemplate.save(Shops);
        mongoTemplate.save(TakeAway);


        DoProducts(PlayStation, Tables, Shops, TakeAway);
        playstationDeviceTypeRepository.deleteAll();
        playstationDeviceRepository.deleteAll();
        DoTables(Tables, Shops, TakeAway);
        DoDevices(PlayStation);

        invoiceRepository.deleteAll();

        invoiceItemRepository.deleteAll();
        playStationSessionRepository.deleteAll();;
         DoTablesRecords( Tables, Shops, TakeAway);
        DoDevicesRecords(PlayStation);
        playstationDeviceService.clearAllDevicesCaches();

*/

    }

    public void DoDevicesTypes() {
        List<DeviceType> results = erMongoTemplate.findAll(DeviceType.class, "device_type");
        playstationDeviceTypeRepository.deleteAll();
        List<PsDeviceType> savedPsDeviceType = new ArrayList<>();
        for (DeviceType deviceType : results) {
            PsDeviceType playstationDeviceTypePricePerHour = new PsDeviceType();
            playstationDeviceTypePricePerHour.setName(deviceType.getName());
            playstationDeviceTypePricePerHour.setPrice(String.valueOf(BigDecimal.valueOf(deviceType.getPricePerHour())));

            savedPsDeviceType.add(playstationDeviceTypeService.save(playstationDeviceTypePricePerHour));

            PsDeviceType playstationDeviceTypePricePerHourMulti = new PsDeviceType();
            playstationDeviceTypePricePerHourMulti.setName(deviceType.getName() + " - Multi");
            playstationDeviceTypePricePerHourMulti.setPrice(String.valueOf(BigDecimal.valueOf(deviceType.getPricePerHour())));

            savedPsDeviceType.add(playstationDeviceTypeService.save(playstationDeviceTypePricePerHourMulti));
        }
    }

    public void DoDevices(PlaystationContainer ps) {
        List<Device> results = erMongoTemplate.findAll(Device.class, "device");

        for (Device device : results) {
            PlaystationDevice playstationDevice = new PlaystationDevice();
            playstationDevice.setName(device.getName());
            playstationDevice.setCategory(ps.getCategory());
            playstationDevice.setActive(false);
            playstationDevice.setTimeManagement(true);
            playstationDevice.setVarRefId(device.getId());

            PsDeviceType playstationDeviceTypePricePerHour = new PsDeviceType();
            playstationDeviceTypePricePerHour.setName(device.getType().getName());
            playstationDeviceTypePricePerHour.setPrice(String.valueOf(BigDecimal.valueOf(device.getType().getPricePerHour())));

            playstationDeviceTypePricePerHour = playstationDeviceTypeService.save(playstationDeviceTypePricePerHour);
            playstationDeviceTypeRepository.findById(playstationDeviceTypePricePerHour.getId()).ifPresent(playstationDevice::setType);

            playstationDeviceTypeRepository
                .findById(playstationDeviceTypePricePerHour.getId())
                .ifPresent(playstationDeviceType -> {
                    playstationDeviceType.setVarRefId(device.getType().getId());
                    playstationDeviceTypeRepository.save(playstationDeviceType);
                });

            PsDeviceType playstationDeviceTypePricePerHourMulti = new PsDeviceType();
            playstationDeviceTypePricePerHourMulti.setName(device.getType().getName() + " - Multi");
            playstationDeviceTypePricePerHourMulti.setPrice(String.valueOf(BigDecimal.valueOf(device.getType().getPricePerHour())));

            playstationDeviceTypeService.save(playstationDeviceTypePricePerHourMulti);
            mongoTemplate.save(playstationDevice);
        }
    }

    public void DoProducts(PlaystationContainer ps, PlaystationContainer tb, PlaystationContainer sh, PlaystationContainer ta) {
        try {
            // Find all products
            List<Product> results = erMongoTemplate.findAll(Product.class, "product");
            log.info("Found {} products to process", results.size());

            itemsRepository.deleteAll();
            // Process the results
            for (Product result : results) {
                try {
                    Pk pk = pkService.generatePkEntity(PkKind.ITEM);

                    Item item = new Item();
                    item.setPk(String.valueOf(pk.getValue().intValue()));
                    item.setName(result.getName());
                    item.setBarcode("result.getBarcode()");
                    item.setPrice1(result.getPrice().toString());
                    item.setCategory(result.getCategory() != null ? result.getCategory().getName() : "");
                    item.setQty(BigDecimal.ZERO);
                    item.setCost(BigDecimal.ZERO);
                    item.setCreatedBy(result.getCreatedBy());
                    item.setCreatedDate(result.getCreatedDate());
                    item.setLastModifiedBy(result.getLastModifiedBy());
                    item.setLastModifiedDate(result.getLastModifiedDate());
                    item.setCheckQty(false);
                    item.setBuildIn(false);
                    item.setDeletable(true);

                    item.setVarRefId(result.getId());

                    ItemPriceOptions itemPriceOptionsPs = new ItemPriceOptions();
                    itemPriceOptionsPs.setName(ps.getName());
                    itemPriceOptionsPs.setValue(BigDecimal.valueOf(result.getPrice()));
                    itemPriceOptionsPs.setRefId(ps.getId());

                    ItemPriceOptions itemPriceOptionsTb = new ItemPriceOptions();
                    itemPriceOptionsTb.setName(tb.getName());
                    itemPriceOptionsTb.setValue(BigDecimal.valueOf(result.getPrice()));
                    itemPriceOptionsTb.setRefId(tb.getId());

                    ItemPriceOptions itemPriceOptionsTa = new ItemPriceOptions();
                    itemPriceOptionsTa.setName(ta.getName());
                    itemPriceOptionsTa.setValue(BigDecimal.valueOf(result.getTakeawayPrice()));
                    itemPriceOptionsTa.setRefId(ta.getId());

                    ItemPriceOptions itemPriceOptionsSh = new ItemPriceOptions();
                    itemPriceOptionsSh.setName(sh.getName());
                    itemPriceOptionsSh.setValue(BigDecimal.valueOf(result.getShopsPrice()));
                    itemPriceOptionsSh.setRefId(sh.getId());

                    item.getPriceOptions().addAll(List.of(itemPriceOptionsPs, itemPriceOptionsTb, itemPriceOptionsTa, itemPriceOptionsSh));

                    // Save the mapped item
                    mongoTemplate.save(item);

                    System.out.println(item.toString());
                } catch (Exception e) {
                    log.error("Error processing product: {}", result.getName(), e);
                }
            }

            ps.acceptedOrderCategories(itemService.getAllItemCategories().stream().map(CategoryItem::getName).collect(Collectors.toSet()));
            tb.acceptedOrderCategories(itemService.getAllItemCategories().stream().map(CategoryItem::getName).collect(Collectors.toSet()));
            sh.acceptedOrderCategories(itemService.getAllItemCategories().stream().map(CategoryItem::getName).collect(Collectors.toSet()));
            ta.acceptedOrderCategories(itemService.getAllItemCategories().stream().map(CategoryItem::getName).collect(Collectors.toSet()));

            playstationContainerRepository.save(ps);
            playstationContainerRepository.save(tb);
            playstationContainerRepository.save(sh);
            playstationContainerRepository.save(ta);
        } catch (Exception e) {
            log.error("Error in DoProducts", e);
        }
    }

    void DoTables(PlaystationContainer tb, PlaystationContainer sh, PlaystationContainer ta) {
        List<Table> results = erMongoTemplate.findAll(Table.class, "table");

        PsDeviceType TABLETYPE = new PsDeviceType();
        TABLETYPE.setName("TABLE");
        TABLETYPE.setPrice(String.valueOf(BigDecimal.ZERO));

        PsDeviceType SHOPSTYPE = new PsDeviceType();
        SHOPSTYPE.setName("SHOPS");
        SHOPSTYPE.setPrice(String.valueOf(BigDecimal.ZERO));

        PsDeviceType TAKEAWAYTYPE = new PsDeviceType();
        TAKEAWAYTYPE.setName("TAKE_AWAY");
        TAKEAWAYTYPE.setPrice(String.valueOf(BigDecimal.ZERO));
        TABLETYPE = playstationDeviceTypeService.save(TABLETYPE);
        SHOPSTYPE = playstationDeviceTypeService.save(SHOPSTYPE);
        TAKEAWAYTYPE = playstationDeviceTypeService.save(TAKEAWAYTYPE);
        for (Table device : results) {
            PlaystationDevice playstationDevice = new PlaystationDevice();
            playstationDevice.setName(device.getName());
            playstationDevice.setVarRefId(device.getId());
            playstationDevice.setActive(false);
            playstationDevice.setTimeManagement(false);

            switch (device.getType()) {
                case SHOPS:
                    {
                        playstationDevice.setCategory(sh.getCategory());
                        playstationDeviceTypeRepository.findById(SHOPSTYPE.getId()).ifPresent(playstationDevice::setType);

                        break;
                    }
                case TABLE:
                    {
                        playstationDevice.setCategory(tb.getCategory());
                        playstationDeviceTypeRepository.findById(TABLETYPE.getId()).ifPresent(playstationDevice::setType);

                        break;
                    }
                case TAKEAWAY:
                    {
                        playstationDevice.setCategory(ta.getCategory());
                        playstationDeviceTypeRepository.findById(TAKEAWAYTYPE.getId()).ifPresent(playstationDevice::setType);
                        break;
                    }
                default:
                    {
                        playstationDevice.setCategory(tb.getCategory());
                        break;
                    }
            }

            mongoTemplate.save(playstationDevice);
        }
    }

    void DoDevicesRecords(PlaystationContainer ps) {
        Query query = new Query(Criteria.where("created_date").gte(Instant.parse("2022-02-26T03:38:32.398+00:00")))
            .with(Pageable.ofSize(recordsRetCount));
        List<Record> results = erMongoTemplate.find(query, Record.class, "record");

        log.debug(results.toString());
        for (Record tableRecord : results) {
            boolean isTable;
            boolean isShops;
            boolean isTakeAway;

            Optional<PlaystationDevice> tableRecord_table_Device = playstationDeviceRepository.findByVarRefId(
                tableRecord.getDevice().getId()
            );

            PlayStationSession tableRecSession = new PlayStationSession();
            tableRecSession.setActive(false);

            if (tableRecord.getStart() != null) {
                tableRecSession.setStartTime(tableRecord.getStart());
            }
            if (tableRecord.getEnd() != null) {
                tableRecSession.setEndTime(tableRecord.getEnd());
            }

            if (tableRecord_table_Device.isPresent()) {
                tableRecSession.setDevice(tableRecord_table_Device.get());
                tableRecSession.setType(tableRecord_table_Device.get().getType());
                tableRecSession.setContainerId(ps.getId());
            }
            tableRecSession = playStationSessionRepository.save(tableRecSession);

            InvoiceDTO inv = invoiceServiceImpl.initializeNewInvoice(InvoiceKind.SALE);

            Invoice invoiceDomain = invoiceServiceImpl.findOneDomain(inv.getId()).get();
            if (tableRecord.getCreatedDate() != null) {
                invoiceDomain.setCreatedDate(tableRecord.getCreatedDate());
                invoiceDomain = invoiceRepository.save(invoiceDomain);
            }

            tableRecSession.setInvoice(invoiceDomain);

            tableRecord
                .getOrdersData()
                .forEach(product -> {
                    Optional<Item> itemOptional = itemsRepository.findByVarRefId(product.getId());

                    if (itemOptional.isPresent()) {
                        try {
                            int qty = tableRecord.getOrdersQuantity().get(product.getId());

                            CreateInvoiceItemDTO createInvoiceItemDTO = new CreateInvoiceItemDTO();

                            createInvoiceItemDTO.setPrice(BigDecimal.valueOf(product.getPrice()));

                            createInvoiceItemDTO.setItemId(itemOptional.get().getId());
                            createInvoiceItemDTO.setQty(BigDecimal.valueOf(qty));
                            InvoiceItem invoiceItem = invoiceServiceImpl.addInvoiceItemDomain(inv.getId(), createInvoiceItemDTO);
                            invoiceItem.setCreatedDate(tableRecord.getCreatedDate());
                            invoiceItemRepository.save(invoiceItem);
                        } catch (Exception e) {}
                    }
                });

            if (tableRecord.getTotalPriceTime() > 0) {
                CreateInvoiceItemDTO createInvoiceItemDTO = new CreateInvoiceItemDTO();
                createInvoiceItemDTO.setItemId(tableRecord_table_Device.get().getType().getItem().getId());
                createInvoiceItemDTO.setPrice(BigDecimal.valueOf(tableRecord.getTotalPriceTime()));
                createInvoiceItemDTO.setQty(BigDecimal.valueOf(1));
                InvoiceItem invoiceItem = invoiceServiceImpl.addInvoiceItemDomain(inv.getId(), createInvoiceItemDTO);
                invoiceItem.setCreatedDate(tableRecord.getCreatedDate());
                invoiceItemRepository.save(invoiceItem);
            }
            if (!invoiceServiceImpl.findOneDomain(inv.getId()).get().getInvoiceItems().isEmpty()) {
                invoiceServiceImpl.saveInvoice(inv.getId());

                Invoice invoiceDomainX = invoiceServiceImpl.findOneDomain(inv.getId()).get();
                invoiceDomainX.setUserNetPrice(BigDecimal.valueOf(tableRecord.getTotalPriceUser()));
                invoiceDomainX = invoiceRepository.save(invoiceDomainX);
                playStationSessionRepository.save(tableRecSession);
            } else {
                invoiceRepository.deleteById(inv.getId());
                playStationSessionRepository.deleteById(tableRecSession.getId());
            }
        }
    }

    void DoTablesRecords(PlaystationContainer tb, PlaystationContainer sh, PlaystationContainer ta) {
        Query query = new Query(Criteria.where("created_date").gte(Instant.parse("2022-02-26T03:38:32.398+00:00")))
            .with(Pageable.ofSize(recordsRetCount));
        List<TableRecord> results = erMongoTemplate.find(query, TableRecord.class, "tableRecord");

        log.debug(results.toString());
        for (TableRecord tableRecord : results) {
            boolean isTable;
            boolean isShops;
            boolean isTakeAway;

            Optional<PlaystationDevice> tableRecord_table_Device = playstationDeviceRepository.findByVarRefId(
                tableRecord.getTable().getId()
            );

            PlayStationSession tableRecSession = new PlayStationSession();
            tableRecSession.setActive(false);

            if (tableRecord.getCreatedDate() != null) {
                tableRecSession.setStartTime(tableRecord.getCreatedDate());
            }
            if (tableRecord.getLastModifiedDate() != null) {
                tableRecSession.setEndTime(tableRecord.getLastModifiedDate());
            }

            if (!tableRecord_table_Device.isEmpty()) {
                tableRecSession.setDevice(tableRecord_table_Device.get());
                tableRecSession.setType(tableRecord_table_Device.get().getType());

                if (tableRecord_table_Device.get().getCategory().equalsIgnoreCase(tb.getCategory())) {
                    tableRecSession.setContainerId(tb.getId());
                    isTable = true;
                } else {
                    isTable = false;
                }
                if (tableRecord_table_Device.get().getCategory().equalsIgnoreCase(ta.getCategory())) {
                    tableRecSession.setContainerId(ta.getId());
                    isTakeAway = true;
                } else {
                    isTakeAway = false;
                }
                if (tableRecord_table_Device.get().getCategory().equalsIgnoreCase(sh.getCategory())) {
                    tableRecSession.setContainerId(sh.getId());
                    isShops = true;
                } else {
                    isShops = false;
                }
            } else {
                isTakeAway = false;
                isShops = false;
                isTable = false;
            }
            tableRecSession = playStationSessionRepository.save(tableRecSession);

            InvoiceDTO inv = invoiceServiceImpl.initializeNewInvoice(InvoiceKind.SALE);

            Invoice invoiceDomain = invoiceServiceImpl.findOneDomain(inv.getId()).get();
            if (tableRecord.getCreatedDate() != null) {
                invoiceDomain.setCreatedDate(tableRecord.getCreatedDate());
                invoiceDomain = invoiceRepository.save(invoiceDomain);
            }

            tableRecSession.setInvoice(invoiceDomain);

            tableRecord
                .getOrdersData()
                .forEach(product -> {
                    Optional<Item> itemOptional = itemsRepository.findByVarRefId(product.getId());

                    if (itemOptional.isPresent()) {
                        try {
                            int qty = tableRecord.getOrdersQuantity().get(product.getId());

                            CreateInvoiceItemDTO createInvoiceItemDTO = new CreateInvoiceItemDTO();

                            if (isTable) {
                                createInvoiceItemDTO.setPrice(BigDecimal.valueOf(product.getPrice()));
                            }
                            if (isShops) {
                                createInvoiceItemDTO.setPrice(BigDecimal.valueOf(product.getShopsPrice()));
                            }

                            if (isTakeAway) {
                                createInvoiceItemDTO.setPrice(BigDecimal.valueOf(product.getTakeawayPrice()));
                            }
                            createInvoiceItemDTO.setItemId(itemOptional.get().getId());
                            createInvoiceItemDTO.setQty(BigDecimal.valueOf(qty));
                            InvoiceItem invoiceItem = invoiceServiceImpl.addInvoiceItemDomain(inv.getId(), createInvoiceItemDTO);
                            invoiceItem.setCreatedDate(tableRecord.getCreatedDate());
                            invoiceItemRepository.save(invoiceItem);
                        } catch (Exception e) {}
                    }
                });
            if (!invoiceServiceImpl.findOneDomain(inv.getId()).get().getInvoiceItems().isEmpty()) {
                invoiceServiceImpl.saveInvoice(inv.getId());

                Invoice invoiceDomainX = invoiceServiceImpl.findOneDomain(inv.getId()).get();
                invoiceDomainX.setUserNetPrice(BigDecimal.valueOf(tableRecord.getNetTotalPrice()));
                invoiceDomainX = invoiceRepository.save(invoiceDomainX);
                playStationSessionRepository.save(tableRecSession);
            } else {
                invoiceRepository.deleteById(inv.getId());
                playStationSessionRepository.deleteById(tableRecSession.getId());
            }
        }
    }
}
