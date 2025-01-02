package com.konsol.core.service.test;

import static org.reflections.Reflections.log;

import com.konsol.core.domain.Item;
import com.konsol.core.domain.ItemPriceOptions;
import com.konsol.core.domain.PlaystationContainer;
import com.konsol.core.domain.VAR.Device;
import com.konsol.core.domain.VAR.DeviceType;
import com.konsol.core.domain.VAR.Product;
import com.konsol.core.domain.playstation.PlaystationDevice;
import com.konsol.core.domain.playstation.PlaystationDeviceType;
import com.konsol.core.repository.ItemRepository;
import com.konsol.core.repository.PlaystationContainerRepository;
import com.konsol.core.repository.PlaystationDeviceRepository;
import com.konsol.core.repository.PlaystationDeviceTypeRepository;
import com.konsol.core.repository.VAR.ProductRepository;
import com.konsol.core.service.*;
import com.konsol.core.service.api.dto.CategoryItem;
import com.konsol.core.service.api.dto.FinancialDashboardDTO;
import com.konsol.core.service.api.dto.PsDeviceType;
import com.konsol.core.service.impl.PlaystationDeviceServiceImpl;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
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
    private final PlaystationDeviceService playstationDeviceServiceImpl;

    private final ItemService itemService;

    public testCMD(
        @Qualifier("erMongoTemplate") MongoTemplate erMongoTemplate,
        MongoTemplate mongoTemplate,
        ItemRepository itemsRepository,
        PlaystationContainerRepository playstationContainerRepository,
        PlaystationDeviceTypeService playstationDeviceTypeService,
        com.konsol.core.repository.PlaystationDeviceTypeRepository playstationDeviceTypeRepository,
        PlaystationDeviceRepository playstationDeviceRepository,
        PlaystationDeviceService playstationDeviceServiceImpl,
        ItemService itemService
    ) {
        this.erMongoTemplate = erMongoTemplate;

        this.mongoTemplate = mongoTemplate;
        this.itemsRepository = itemsRepository;
        this.playstationContainerRepository = playstationContainerRepository;
        this.playstationDeviceTypeService = playstationDeviceTypeService;
        this.playstationDeviceTypeRepository = playstationDeviceTypeRepository;
        this.playstationDeviceRepository = playstationDeviceRepository;
        this.playstationDeviceServiceImpl = playstationDeviceServiceImpl;
        this.itemService = itemService;
    }

    /**
     * @param args incoming main method arguments
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
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
            .orderSelectedPriceCategory("PlayStation")
            .acceptedOrderCategories(Set.of("PS4", "PS4_PRO"));

        PlaystationContainer Tables = new PlaystationContainer()
            .name("Tables")
            .category("Tables")
            .defaultIcon("ps4pro-icon")
            .hasTimeManagement(true)
            .showType(true)
            .showTime(true)
            .canMoveDevice(true)
            .canHaveMultiTimeManagement(true)
            .orderSelectedPriceCategory("Tables")
            .acceptedOrderCategories(Set.of("PS4", "PS4_PRO"));

        PlaystationContainer Shops = new PlaystationContainer()
            .name("Shops")
            .category("Shops")
            .defaultIcon("ps5d-icon")
            .hasTimeManagement(true)
            .showType(true)
            .showTime(true)
            .canMoveDevice(true)
            .canHaveMultiTimeManagement(true)
            .orderSelectedPriceCategory("Shops")
            .acceptedOrderCategories(Set.of("PS5"));

        PlaystationContainer TakeAway = new PlaystationContainer()
            .name("TakeAway")
            .category("TakeAway")
            .defaultIcon("ps5-icon")
            .hasTimeManagement(true)
            .showType(true)
            .showTime(true)
            .canMoveDevice(true)
            .canHaveMultiTimeManagement(true)
            .orderSelectedPriceCategory("TakeAway")
            .acceptedOrderCategories(Set.of("PS5"));

        // Save all containers using mongoTemplate
        mongoTemplate.save(PlayStation);
        mongoTemplate.save(Tables);
        mongoTemplate.save(Shops);
        mongoTemplate.save(TakeAway);

        DoProducts(PlayStation, Tables, Shops, TakeAway);
        DoDevices(PlayStation);
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
        playstationDeviceTypeRepository.deleteAll();
        playstationDeviceRepository.deleteAll();
        for (Device device : results) {
            PlaystationDevice playstationDevice = new PlaystationDevice();
            playstationDevice.setName(device.getName());
            playstationDevice.setCategory(ps.getCategory());
            playstationDevice.setActive(false);
            playstationDevice.setTimeManagement(true);

            PsDeviceType playstationDeviceTypePricePerHour = new PsDeviceType();
            playstationDeviceTypePricePerHour.setName(device.getType().getName());
            playstationDeviceTypePricePerHour.setPrice(String.valueOf(BigDecimal.valueOf(device.getType().getPricePerHour())));

            playstationDeviceTypePricePerHour = playstationDeviceTypeService.save(playstationDeviceTypePricePerHour);
            playstationDeviceTypeRepository.findById(playstationDeviceTypePricePerHour.getId()).ifPresent(playstationDevice::setType);

            PsDeviceType playstationDeviceTypePricePerHourMulti = new PsDeviceType();
            playstationDeviceTypePricePerHourMulti.setName(device.getType().getName() + " - Multi");
            playstationDeviceTypePricePerHourMulti.setPrice(String.valueOf(BigDecimal.valueOf(device.getType().getPricePerHour())));

            playstationDeviceTypeService.save(playstationDeviceTypePricePerHourMulti);
            mongoTemplate.save(playstationDevice);
        }
        playstationDeviceServiceImpl.clearAllDevicesCaches();
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
                    Item item = new Item();
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
}
