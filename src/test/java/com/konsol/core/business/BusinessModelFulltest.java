package com.konsol.core.business;

import static org.junit.jupiter.api.Assertions.*;

import com.konsol.core.IntegrationTest;
import com.konsol.core.domain.Invoice;
import com.konsol.core.domain.Settings;
import com.konsol.core.domain.enumeration.InvoiceKind;
import com.konsol.core.service.InvoiceService;
import com.konsol.core.service.ItemService;
import com.konsol.core.service.SettingService;
import com.konsol.core.service.StoreService;
import com.konsol.core.service.api.dto.CreateInvoiceItemDTO;
import com.konsol.core.service.api.dto.InvoiceDTO;
import com.konsol.core.service.api.dto.InvoiceUpdateDTO;
import com.konsol.core.service.api.dto.ItemDTO;
import com.konsol.core.web.rest.api.errors.ItemQtyException;
import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.ConfigurationParameters;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfiguration;
import org.junit.platform.engine.support.hierarchical.ParallelExecutionConfigurationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;

//@IntegrationTest
@SpringBootTest
public class BusinessModelFulltest {

    @Autowired
    ItemService itemService;

    @Autowired
    InvoiceService invoiceService;

    @Autowired
    StoreService storeService;

    @Autowired
    SettingService settingService;

    private ItemDTO itemOne, itemTwo;

    @BeforeEach
    public void init() {
        Settings settings = settingService.getSettings();
        SecurityContextHolder.clearContext();

        /**
         * Create New Item With Check QTY Option
         */
        ItemDTO itemWithCheckQTY = new ItemDTO();
        itemWithCheckQTY.name("Apple iPhone 14");
        itemWithCheckQTY.price1("999");
        itemWithCheckQTY.price2("999");
        itemWithCheckQTY.cost(new BigDecimal(650));
        itemWithCheckQTY.setQty(new BigDecimal(50));
        itemWithCheckQTY.setCategory("Electronics");
        itemWithCheckQTY.checkQty(true);
        //SAVES ITEM
        ItemDTO Item1 = itemService.create(itemWithCheckQTY);

        /**
         * Create New Item With No Check QTY Option
         */
        ItemDTO itemWithNoCheckQTY = new ItemDTO();
        itemWithNoCheckQTY.name("Nike Air Max 270");
        itemWithNoCheckQTY.price1("150");
        itemWithNoCheckQTY.price2("150");
        itemWithNoCheckQTY.cost(new BigDecimal(90));
        itemWithNoCheckQTY.setQty(new BigDecimal(120));
        itemWithNoCheckQTY.setCategory("Footwear");
        itemWithNoCheckQTY.checkQty(false);

        //SAVES ITEM
        ItemDTO Item2 = itemService.create(itemWithNoCheckQTY);

        Assertions.assertNotNull(Item1.getId());
        Assertions.assertNotNull(Item2.getId());

        itemOne = Item1;
        itemTwo = Item2;

        System.out.println("Created Item1 " + Item1.getId() + " name : " + Item1.getName());
        System.out.println("Created Item2 " + Item2.getId() + " name : " + Item2.getName());
    }

    @Test
    public void CreateAndValidateSaleInvoice() {
        Assertions.assertNotNull(itemOne, "Item not created");
        Assertions.assertNotNull(itemTwo, "Item not created");

        storeService.addItemQtyToStores(itemOne.getId(), new BigDecimal(50), null);
        storeService.addItemQtyToStores(itemTwo.getId(), new BigDecimal(120), null);

        Assertions.assertEquals(0, storeService.getItemQty(itemOne.getId()).compareTo(new BigDecimal(50)));
        Assertions.assertEquals(0, storeService.getItemQty(itemTwo.getId()).compareTo(new BigDecimal(120)));

        /**
         * Creating Sale Invoice
         * Sell 20 items
         * check items qty in store
         */
        System.out.println("------------------------ initializeNewInvoice -------------------------------");
        InvoiceDTO invoiceDTO = invoiceService.initializeNewInvoice(InvoiceKind.SALE);
        System.out.println("------------------------ addInvoiceItem -------------------------------");
        CreateInvoiceItemDTO createInvoiceItemDTO = new CreateInvoiceItemDTO();
        createInvoiceItemDTO.itemId(itemOne.getId());
        createInvoiceItemDTO.setPrice(new BigDecimal(itemOne.getPrice1()));
        createInvoiceItemDTO.setQty(new BigDecimal(20));
        invoiceDTO = invoiceService.addInvoiceItem(invoiceDTO.getId(), createInvoiceItemDTO);

        System.out.println("Make Sure Sale Invoice Has 20 * ItemOne price in invoice TotalPrice");
        Assertions.assertEquals(new BigDecimal(itemOne.getPrice1()).multiply(new BigDecimal(20)), invoiceDTO.getTotalPrice());
        System.out.println("Make Sure Sale Invoice Has 20 * ItemOne price in invoice NetPrice");
        Assertions.assertEquals(new BigDecimal(itemOne.getPrice1()).multiply(new BigDecimal(20)), invoiceDTO.getNetPrice());

        /**
         * Updating Invoice
         * adding some discount
         */

        InvoiceUpdateDTO invoiceUpdateDTO = new InvoiceUpdateDTO();
        invoiceUpdateDTO.setId(invoiceDTO.getId());
        invoiceUpdateDTO.setDiscount(new BigDecimal(20));

        System.out.println("------------------------ setDiscount -------------------------------");
        Invoice invoice = invoiceService.updateInvoice(invoiceUpdateDTO);
        System.out.println("Make Sure Sale Invoice Has 20 * ItemOne price in invoice TotalPrice Not Changed After Discount");
        Assertions.assertEquals(new BigDecimal(itemOne.getPrice1()).multiply(new BigDecimal(20)), invoice.getTotalPrice());
        System.out.println("Make Sure Sale Invoice Has 20 * ItemOne price in invoice NetPrice - discount");
        Assertions.assertEquals(
            new BigDecimal(itemOne.getPrice1()).multiply(new BigDecimal(20)).subtract(new BigDecimal(20)),
            invoice.getNetPrice()
        );

        System.out.println("------------------------ Saving Invoice -------------------------------");

        invoice = invoiceService.saveInvoice(invoice.getId());

        System.out.println("Done");
        System.out.println(invoice.toString());
        System.out.println("------------------------ Checking Store -------------------------------");
        Assertions.assertEquals(0, storeService.getItemQty(itemOne.getId()).compareTo(new BigDecimal(30)));
        System.out.println("Done");
    }

    @Test
    public void CreateAndValidatePurchaseInvoice() {
        Assertions.assertNotNull(itemOne, "Item not created");
        Assertions.assertNotNull(itemTwo, "Item not created");

        storeService.addItemQtyToStores(itemOne.getId(), new BigDecimal(50), null);
        storeService.addItemQtyToStores(itemTwo.getId(), new BigDecimal(120), null);

        Assertions.assertEquals(0, storeService.getItemQty(itemOne.getId()).compareTo(new BigDecimal(50)));
        Assertions.assertEquals(0, storeService.getItemQty(itemTwo.getId()).compareTo(new BigDecimal(120)));

        /**
         * Creating Sale Invoice
         * buy 20 items
         * check items qty in store
         */
        System.out.println("------------------------ initializeNewInvoice -------------------------------");
        InvoiceDTO invoiceDTO = invoiceService.initializeNewInvoice(InvoiceKind.PURCHASE);
        System.out.println("------------------------ addInvoiceItem -------------------------------");
        CreateInvoiceItemDTO createInvoiceItemDTO = new CreateInvoiceItemDTO();
        createInvoiceItemDTO.itemId(itemOne.getId());
        createInvoiceItemDTO.setPrice(itemOne.getCost());

        createInvoiceItemDTO.setQty(new BigDecimal(20));
        invoiceDTO = invoiceService.addInvoiceItem(invoiceDTO.getId(), createInvoiceItemDTO);

        System.out.println("Make Sure Purchase Invoice Has 20 * ItemOne price in invoice TotalCost");
        Assertions.assertEquals(itemOne.getCost().multiply(new BigDecimal(20)), invoiceDTO.getTotalCost());
        System.out.println("Make Sure Purchase Invoice Has 20 * ItemOne price in invoice NetCost");
        Assertions.assertEquals(itemOne.getCost().multiply(new BigDecimal(20)), invoiceDTO.getNetCost());

        /**
         * Updating Invoice
         * adding some discount
         */

        InvoiceUpdateDTO invoiceUpdateDTO = new InvoiceUpdateDTO();
        invoiceUpdateDTO.setId(invoiceDTO.getId());
        invoiceUpdateDTO.setDiscount(new BigDecimal(20));

        System.out.println("------------------------ setDiscount -------------------------------");
        Invoice invoice = invoiceService.updateInvoice(invoiceUpdateDTO);
        System.out.println("Make Sure Sale Invoice Has 20 * ItemOne price in invoice TotalPrice Not Changed After Discount");
        Assertions.assertEquals(itemOne.getCost().multiply(new BigDecimal(20)), invoice.getTotalCost());
        System.out.println("Make Sure Sale Invoice Has 20 * ItemOne price in invoice NetPrice - discount");
        Assertions.assertEquals(itemOne.getCost().multiply(new BigDecimal(20)).subtract(new BigDecimal(20)), invoice.getNetCost());

        System.out.println("------------------------ Saving Invoice -------------------------------");

        invoice = invoiceService.saveInvoice(invoice.getId());

        System.out.println("Done");
        System.out.println(invoice.toString());
        System.out.println("------------------------ Checking Store -------------------------------");
        Assertions.assertEquals(0, storeService.getItemQty(itemOne.getId()).compareTo(new BigDecimal(70)));
        System.out.println("Done");
    }

    @Test
    public void CreateAndValidateSaleInvoiceItemNoqty() {
        /**
         * Creating Sale Invoice
         * Sell 20 items
         * check items qty in store
         */
        System.out.println("------------------------ initializeNewInvoice -------------------------------");
        InvoiceDTO invoiceDTO = invoiceService.initializeNewInvoice(InvoiceKind.SALE);
        System.out.println("------------------------ addInvoiceItem -------------------------------");
        CreateInvoiceItemDTO createInvoiceItemDTO = new CreateInvoiceItemDTO();
        createInvoiceItemDTO.itemId(itemTwo.getId());
        createInvoiceItemDTO.setPrice(new BigDecimal(itemTwo.getPrice1()));
        createInvoiceItemDTO.setQty(new BigDecimal(20));
        invoiceDTO = invoiceService.addInvoiceItem(invoiceDTO.getId(), createInvoiceItemDTO);

        System.out.println("Make Sure Sale Invoice Has 20 * ItemOne price in invoice TotalPrice");
        Assertions.assertEquals(new BigDecimal(itemTwo.getPrice1()).multiply(new BigDecimal(20)), invoiceDTO.getTotalPrice());
        System.out.println("Make Sure Sale Invoice Has 20 * ItemOne price in invoice NetPrice");
        Assertions.assertEquals(new BigDecimal(itemTwo.getPrice1()).multiply(new BigDecimal(20)), invoiceDTO.getNetPrice());

        /**
         * Updating Invoice
         * adding some discount
         */

        InvoiceUpdateDTO invoiceUpdateDTO = new InvoiceUpdateDTO();
        invoiceUpdateDTO.setId(invoiceDTO.getId());
        invoiceUpdateDTO.setDiscount(new BigDecimal(20));

        System.out.println("------------------------ setDiscount -------------------------------");
        Invoice invoice = invoiceService.updateInvoice(invoiceUpdateDTO);

        System.out.println("------------------------ Saving Invoice -------------------------------");

        invoice = invoiceService.saveInvoice(invoice.getId());

        System.out.println("Done");
        System.out.println(invoice.toString());
    }

    @Test
    public void CreateAndValidatePurchaseInvoiceItemNoQty() {
        /**
         * Creating Sale Invoice
         * buy 20 items
         * check items qty in store
         */
        System.out.println("------------------------ initializeNewInvoice -------------------------------");
        InvoiceDTO invoiceDTO = invoiceService.initializeNewInvoice(InvoiceKind.PURCHASE);
        System.out.println("------------------------ addInvoiceItem -------------------------------");
        CreateInvoiceItemDTO createInvoiceItemDTO = new CreateInvoiceItemDTO();
        createInvoiceItemDTO.itemId(itemTwo.getId());
        createInvoiceItemDTO.setPrice(itemTwo.getCost());

        createInvoiceItemDTO.setQty(new BigDecimal(20));
        invoiceDTO = invoiceService.addInvoiceItem(invoiceDTO.getId(), createInvoiceItemDTO);

        System.out.println("Make Sure Purchase Invoice Has 20 * ItemOne price in invoice TotalCost");
        Assertions.assertEquals(itemTwo.getCost().multiply(new BigDecimal(20)), invoiceDTO.getTotalCost());
        System.out.println("Make Sure Purchase Invoice Has 20 * ItemOne price in invoice NetCost");
        Assertions.assertEquals(itemTwo.getCost().multiply(new BigDecimal(20)), invoiceDTO.getNetCost());

        /**
         * Updating Invoice
         * adding some discount
         */

        InvoiceUpdateDTO invoiceUpdateDTO = new InvoiceUpdateDTO();
        invoiceUpdateDTO.setId(invoiceDTO.getId());
        invoiceUpdateDTO.setDiscount(new BigDecimal(20));

        System.out.println("------------------------ setDiscount -------------------------------");
        Invoice invoice = invoiceService.updateInvoice(invoiceUpdateDTO);

        System.out.println("------------------------ Saving Invoice -------------------------------");

        invoice = invoiceService.saveInvoice(invoice.getId());

        System.out.println("Done");
        System.out.println(invoice.toString());
    }

    @Test
    public void CreateAndValidateSaleInvoiceItemQtyCheck() {
        storeService.addItemQtyToStores(itemOne.getId(), new BigDecimal(20), null);

        /**
         * Creating Sale Invoice
         * Sell 20 items
         * check items qty in store
         */
        System.out.println("------------------------ initializeNewInvoice -------------------------------");
        InvoiceDTO invoiceDTO = invoiceService.initializeNewInvoice(InvoiceKind.SALE);
        System.out.println("------------------------ addInvoiceItem -------------------------------");
        CreateInvoiceItemDTO createInvoiceItemDTO = new CreateInvoiceItemDTO();
        createInvoiceItemDTO.itemId(itemOne.getId());
        createInvoiceItemDTO.setPrice(new BigDecimal(itemOne.getPrice1()));
        createInvoiceItemDTO.setQty(new BigDecimal(500));

        try {
            invoiceService.addInvoiceItem(invoiceDTO.getId(), createInvoiceItemDTO);
            fail();
        } catch (Exception e) {
            Assertions.assertEquals(e.getClass().getTypeName(), org.zalando.problem.DefaultProblem.class.getTypeName());
        }
    }

    @Test
    public void AddingItemsToStore() {
        Assertions.assertNotNull(itemOne, "Item not created");
        Assertions.assertNotNull(itemTwo, "Item not created");

        storeService.addItemQtyToStores(itemOne.getId(), new BigDecimal(50), null);
        storeService.addItemQtyToStores(itemTwo.getId(), new BigDecimal(120), null);

        Assertions.assertEquals(0, storeService.getItemQty(itemOne.getId()).compareTo(new BigDecimal(50)));
        Assertions.assertEquals(0, storeService.getItemQty(itemTwo.getId()).compareTo(new BigDecimal(120)));

        System.out.println("Added Item1 (" + itemOne.getId() + " name : " + itemOne.getName() + ") - To Store");
        System.out.println("Added Item2 (" + itemTwo.getId() + " name : " + itemTwo.getName() + ") - To Store");
    }
}
