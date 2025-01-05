package com.konsol.core.service.impl;

import com.konsol.core.domain.*;
import com.konsol.core.domain.enumeration.InvoiceKind;
import com.konsol.core.domain.enumeration.PkKind;
import com.konsol.core.repository.InvoiceItemRepository;
import com.konsol.core.repository.InvoiceRepository;
import com.konsol.core.service.*;
import com.konsol.core.service.api.dto.*;
import com.konsol.core.service.core.Interface.PurchaseService;
import com.konsol.core.service.core.Interface.SaleService;
import com.konsol.core.service.core.query.MongoQueryService;
import com.konsol.core.service.mapper.InvoiceItemMapper;
import com.konsol.core.service.mapper.InvoiceMapper;
import com.konsol.core.web.rest.api.errors.InvoiceException;
import com.konsol.core.web.rest.api.errors.InvoiceNotFoundException;
import com.konsol.core.web.rest.api.errors.ItemNotFoundException;
import com.konsol.core.web.rest.api.errors.ItemQtyException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.bson.Document;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Invoice}.
 */
@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final Logger log = LoggerFactory.getLogger(InvoiceServiceImpl.class);

    private final InvoiceRepository invoiceRepository;

    private final InvoiceMapper invoiceMapper;

    private final PkService pkService;

    private final ItemService itemService;

    private final StoreService storeService;

    private final BankService bankService;

    private final AccountUserService accountUserService;

    protected final InvoiceItemRepository invoiceItemRepository;

    private final MongoQueryService mongoQueryService;
    private final InvoiceItemMapper invoiceItemMapper;

    @Qualifier(value = "SALES")
    private final SaleService saleService;

    @Qualifier(value = "PURCHASE")
    private final PurchaseService purchaseService;

    private final MongoTemplate mongoTemplate;

    private final SettingService settingService;

    private final MoneyService moneyService;

    public InvoiceServiceImpl(
        InvoiceRepository invoiceRepository,
        InvoiceMapper invoiceMapper,
        PkService pkService,
        ItemService itemService,
        StoreService storeService,
        BankService bankService,
        AccountUserService accountUserService,
        InvoiceItemRepository invoiceItemRepository,
        MongoQueryService mongoQueryService,
        InvoiceItemMapper invoiceItemMapper,
        SaleService saleService,
        PurchaseService purchaseService,
        MongoTemplate mongoTemplate,
        SettingService settingService,
        MoneyService moneyService
    ) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.pkService = pkService;
        this.itemService = itemService;
        this.storeService = storeService;
        this.bankService = bankService;
        this.accountUserService = accountUserService;
        this.invoiceItemRepository = invoiceItemRepository;
        this.mongoQueryService = mongoQueryService;
        this.invoiceItemMapper = invoiceItemMapper;
        this.saleService = saleService;
        this.purchaseService = purchaseService;
        this.mongoTemplate = mongoTemplate;
        this.settingService = settingService;
        this.moneyService = moneyService;
    }

    /**
     * Updates an invoice partially based on the provided InvoiceUpdateDTO.
     *
     * @param invoiceUpdateDTO The DTO containing the updated information for the invoice
     * @return The updated invoice entity
     */
    @Override
    public Invoice updateInvoice(InvoiceUpdateDTO invoiceUpdateDTO) {
        log.debug("Request to partially update Invoice : {}", invoiceUpdateDTO);

        if (
            invoiceUpdateDTO.getAccountId() != null &&
            !invoiceUpdateDTO.getAccountId().isEmpty() &&
            !invoiceUpdateDTO.getAccountId().equals("null")
        ) {
            accountUserService
                .findOneDomain(invoiceUpdateDTO.getAccountId())
                .ifPresentOrElse(
                    account ->
                        invoiceRepository
                            .findById(invoiceUpdateDTO.getId())
                            .ifPresentOrElse(
                                existingInvoice -> invoiceRepository.save(existingInvoice.account(account)),
                                () -> {
                                    throw new InvoiceNotFoundException(invoiceUpdateDTO.getId());
                                }
                            ),
                    () -> {
                        throw new InvoiceException("account not found", "account Could not be added to invoice");
                    }
                );
        } else if (invoiceUpdateDTO.getAccountId() != null && invoiceUpdateDTO.getAccountId().equals("null")) {
            invoiceRepository
                .findById(invoiceUpdateDTO.getId())
                .ifPresentOrElse(existingInvoice -> invoiceRepository.save(existingInvoice.account(null)), () -> {});
        }

        if (invoiceUpdateDTO.getBankId() != null && !invoiceUpdateDTO.getBankId().isEmpty()) {
            bankService
                .findOneDomain(invoiceUpdateDTO.getBankId())
                .ifPresentOrElse(
                    bank ->
                        invoiceRepository
                            .findById(invoiceUpdateDTO.getId())
                            .ifPresentOrElse(
                                existingInvoice -> invoiceRepository.save(existingInvoice.bank(bank)),
                                () -> {
                                    throw new InvoiceNotFoundException(invoiceUpdateDTO.getId());
                                }
                            ),
                    () -> {
                        throw new InvoiceException("store not found", "store Could not be added to invoice");
                    }
                );
        }

        if (invoiceUpdateDTO.getStoreId() != null && !invoiceUpdateDTO.getStoreId().isEmpty()) {
            storeService
                .findOneDomain(invoiceUpdateDTO.getStoreId())
                .ifPresentOrElse(
                    store ->
                        invoiceRepository
                            .findById(invoiceUpdateDTO.getId())
                            .ifPresentOrElse(
                                existingInvoice -> invoiceRepository.save(existingInvoice.store(store)),
                                () -> {
                                    throw new InvoiceNotFoundException(invoiceUpdateDTO.getId());
                                }
                            ),
                    () -> {
                        throw new InvoiceException("store not found", "store Could not be added to invoice");
                    }
                );
        }

        return invoiceRepository
            .findById(invoiceUpdateDTO.getId())
            .map(existingInvoice -> {
                invoiceMapper.partialInvoiceUpdate(existingInvoice, invoiceUpdateDTO);
                return calcInvoice(existingInvoice, true);
            })
            .orElseGet(null);
    }

    @Override
    public Page<InvoiceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Invoices");
        return invoiceRepository.findAll(pageable).map(invoiceMapper::toDto);
    }

    @Override
    public Optional<InvoiceDTO> findOne(String id) {
        log.debug("Request to get Invoice : {}", id);
        return invoiceRepository.findById(id).map(invoiceMapper::toDto);
    }

    /**
     * Get the "id" invoice.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<Invoice> findOneDomain(String id) {
        return this.invoiceRepository.findById(id);
    }

    /**
     * Delete the "id" invoice.
     * and delete all invoice items
     * @param id the id of the entity.
     */
    @Override
    public void delete(String id) {
        log.debug("Request to delete Invoice : {}", id);
        Optional<Invoice> invoiceOp = invoiceRepository.findById(id);
        invoiceOp.ifPresent(invoice -> {
            invoiceItemRepository.deleteAll(invoice.getInvoiceItems());
            invoiceRepository.deleteById(id);
        });
    }

    /**
     * @return
     */
    @Override
    public InvoiceMapper getMapper() {
        return this.invoiceMapper;
    }

    /**
     * Initializes a new invoice with the specified kind.
     *
     * @param kind The kind of the invoice
     * @return The DTO representation of the newly initialized invoice
     */
    @Override
    public InvoiceDTO initializeNewInvoice(InvoiceKind kind) {
        return invoiceMapper.toDto(initializeNewInvoiceDomein(kind));
    }

    @Override
    public Invoice initializeNewInvoiceDomein(InvoiceKind kind) {
        Invoice invoice = new Invoice();
        invoice.setActive(false);
        Pk pk = pkService.getPkEntity(PkKind.INVOICE);
        invoice.setPk(pk.getValue().add(new BigDecimal(1)).intValue() + "");
        invoice.setKind(kind);
        return invoiceRepository.save(invoice);
    }

    /**
     * @param kind kind of new created invoice
     * @return
     */
    @Override
    public InvoiceDTO createInvoice(InvoiceKind kind) {
        String invoiceId = initializeNewInvoice(kind).getId();
        Optional<Invoice> invoiceOptional = findOneDomain(invoiceId);
        if (invoiceOptional.isEmpty()) {
            return null;
        } else {
            Invoice invoiceFound = invoiceOptional.get();
            invoiceFound.setActive(true);
            invoiceFound = invoiceRepository.save(invoiceFound);
            return invoiceMapper.toDto(invoiceFound);
        }
    }

    /**
     * Sets the unit details for the given invoice item based on the provided parameters.
     *
     * @param invoiceItem The invoice item to set the unit for
     * @param unitId The ID of the unit to set
     * @param userQty The quantity of the unit
     * @param userPrice The price of the unit
     * @return The updated invoice item with the unit details set
     */
    @Override
    public InvoiceItem setInvoiceItemUnit(InvoiceItem invoiceItem, String unitId, BigDecimal userQty, BigDecimal userPrice) {
        if (unitId != null) {
            Optional<ItemUnit> itemUnitOp = itemService.getUnitItemById(unitId);
            if (itemUnitOp.isPresent()) {
                invoiceItem.setItemUnit(itemUnitOp.get());
                invoiceItem.setUnit(itemUnitOp.get().getName());
                invoiceItem.setUnitPieces(itemUnitOp.get().getPieces());
                invoiceItem.setUnitPrice(userPrice);
                invoiceItem.setUnitCost(invoiceItem.getCost());
                return invoiceItem;
            }
        }
        return setInvoiceItemNullUnit(invoiceItem);
    }

    /**
     * Initializes a new invoice item based on the provided parameters.
     *
     * @param kind The kind of invoice item
     * @param ItemId The ID of the item
     * @param unitId The ID of the unit
     * @param userQty The quantity of the item
     * @param userPrice The price of the item
     * @return The newly initialized invoice item
     * @throws ItemNotFoundException if the item with the provided ID is not found
     */
    @Override
    public InvoiceItem initializeNewInvoiceItem(InvoiceKind kind, String ItemId, String unitId, BigDecimal userQty, BigDecimal userPrice) {
        Optional<Item> itemOp = itemService.findOneById(ItemId);
        InvoiceItem invoiceItem = new InvoiceItem();
        /**
         * item
         */
        if (itemOp.isEmpty()) {
            throw new ItemNotFoundException(null);
        }
        invoiceItem.setItem(itemOp.get());

        /**
         * QTY COST PRICE
         */
        invoiceItem.setUserQty(userQty);
        switch (kind) {
            case SALE:
                invoiceItem.setPrice(userPrice);
                invoiceItem.setCost(invoiceItem.getItem().getCost());
                break;
            case PURCHASE:
                invoiceItem.setCost(userPrice);
                invoiceItem.setPrice(new BigDecimal(invoiceItem.getItem().getPrice1()));
                break;
        }

        /**
         *  Unit
         */
        setInvoiceItemUnit(invoiceItem, unitId, userQty, userPrice);

        invoiceItem.setBuildIn(invoiceItem.getItem().isBuildIn());
        return this.invoiceItemRepository.save(invoiceItem);
    }

    /**
     * Calculates the invoice item based on the type of invoice (sale or purchase).
     *
     * @param invoiceItem The invoice item to calculate
     * @return The calculated invoice item
     */
    @Override
    public InvoiceItem calcInvoiceInvoiceItem(InvoiceItem invoiceItem) {
        Optional<Invoice> invoiceOp = invoiceRepository.findById(invoiceItem.getInvoiceId());

        /**
         * invoice
         */
        if (!invoiceOp.isPresent()) {
            return invoiceItem;
        }

        if (invoiceOp.get().getKind() == InvoiceKind.SALE) {
            return invoiceItemRepository.save(saleService.calcInvoiceItem(invoiceItem));
        }
        if (invoiceOp.get().getKind() == InvoiceKind.PURCHASE) {
            return invoiceItemRepository.save(purchaseService.calcInvoiceItem(invoiceItem));
        }
        return invoiceItemRepository.save(invoiceItem);
    }

    /**
     * Creates a new invoice item based on the provided invoice and item details.
     *
     * @param invoice The invoice to which the item belongs
     * @param createInvoiceItemDTO The DTO containing details of the item to be created
     * @return The newly created invoice item
     */
    @Override
    public InvoiceItem createInvoiceItem(Invoice invoice, CreateInvoiceItemDTO createInvoiceItemDTO) {
        /**
         * create invoice item add unit price and qty required
         */

        InvoiceItem invoiceItem = initializeNewInvoiceItem(
            invoice.getKind(),
            createInvoiceItemDTO.getItemId(),
            createInvoiceItemDTO.getUnitId(),
            createInvoiceItemDTO.getQty(),
            createInvoiceItemDTO.getPrice()
        );

        /**
         * set invoice id
         */
        invoiceItem.setInvoiceId(invoice.getId());

        /**
         *  calc invoice in, out qty and total price
         */

        InvoiceItem savedInvoiceitem = calcInvoiceInvoiceItem(invoiceItem);

        invoice.getInvoiceItems().add(savedInvoiceitem);

        Invoice invoiceSaved = invoiceRepository.save(invoice);

        this.calcInvoice(invoiceSaved, true);

        return savedInvoiceitem;
    }

    /**
     * Calculates the quantity of items in or out in the invoice items based on the provided parameters.
     *
     * @param invoiceId The ID of the invoice
     * @param itemId The ID of the item
     * @param out A boolean indicating whether to calculate the quantity out (true) or in (false)
     * @return The calculated quantity as a BigDecimal
     */
    @Override
    public BigDecimal calcItemQtyOutInInvoiceItems(String invoiceId, String itemId, boolean out) {
        /**
         * [{
         *     $match: {
         *         'item.$id': ObjectId('63277b56cf7d214935b27b78'),
         *         invoice_id: '6339667aae22b238cd7174db'
         *     }
         * }, {
         *     $group: {
         *         _id: 'InvoiceItemsTotalQtyForItem',
         *         totalQtyOut: {
         *             $sum: {
         *                 $convert: {
         *                     input: '$qty_out',
         *                     to: 'decimal',
         *                     onError: 0,
         *                     onNull: 0
         *                 }
         *             }
         *         },
         *         totalQtyIn: {
         *             $sum: {
         *                 $convert: {
         *                     input: '$qty_In',
         *                     to: 'decimal',
         *                     onError: 0,
         *                     onNull: 0
         *                 }
         *             }
         *         }
         *     }
         * }]
         */

        MongoCollection<Document> collection = mongoTemplate.getCollection("invoice_items");
        AggregateIterable<Document> result = collection.aggregate(
            Arrays.asList(
                new Document("$match", new Document("item.$id", new ObjectId(itemId)).append("invoice_id", invoiceId)),
                new Document(
                    "$group",
                    new Document("_id", "InvoiceItemsTotalQtyForItem")
                        .append(
                            "totalQtyOut",
                            new Document(
                                "$sum",
                                new Document(
                                    "$convert",
                                    new Document("input", "$qty_out").append("to", "decimal").append("onError", 0L).append("onNull", 0L)
                                )
                            )
                        )
                        .append(
                            "totalQtyIn",
                            new Document(
                                "$sum",
                                new Document(
                                    "$convert",
                                    new Document("input", "$qty_In").append("to", "decimal").append("onError", 0L).append("onNull", 0L)
                                )
                            )
                        )
                )
            )
        );

        MongoCursor<Document> iterator = result.iterator();

        if (iterator.hasNext()) {
            var tResult = iterator.next();
            if (out) {
                Decimal128 totalQtyOut = Decimal128.parse(tResult.get("totalQtyOut").toString());
                return totalQtyOut.bigDecimalValue();
            } else {
                Decimal128 totalQtyIn = Decimal128.parse(tResult.get("totalQtyIn").toString());
                return totalQtyIn.bigDecimalValue();
            }
        } else {
            return new BigDecimal(0);
        }
    }

    /**
     * Adds an invoice item to the specified invoice.
     *
     * @param invoiceId The ID of the invoice to add the item to
     * @param createInvoiceItemDTO The DTO containing information about the item to add
     * @return The updated InvoiceDTO after adding the item
     */
    @Override
    public InvoiceDTO addInvoiceItem(String invoiceId, CreateInvoiceItemDTO createInvoiceItemDTO) {
        Optional<Invoice> invoiceOp = invoiceRepository.findById(invoiceId);

        /**
         * invoice
         */
        if (invoiceOp.isEmpty()) {
            throw new InvoiceNotFoundException(null);
        }

        Settings settings = settingService.getSettings();

        boolean isPriceCalc = invoiceOp.get().getKind().equals(InvoiceKind.SALE);
        boolean isCostCalc = invoiceOp.get().getKind().equals(InvoiceKind.PURCHASE);

        boolean checkQty = isPriceCalc && settings.isSALES_CHECK_ITEM_QTY();

        InvoiceItem invFound = null;
        if (createInvoiceItemDTO.getUnitId() != null) {
            invFound =
                findInvoiceItemByItemIdAndUnitIdAndMoney(
                    invoiceOp.get().getId(),
                    createInvoiceItemDTO.getItemId(),
                    createInvoiceItemDTO.getUnitId(),
                    createInvoiceItemDTO.getPrice(),
                    isPriceCalc
                );
        } else {
            invFound =
                findInvoiceItemByItemIdAndMoney(
                    invoiceOp.get().getId(),
                    createInvoiceItemDTO.getItemId(),
                    createInvoiceItemDTO.getPrice(),
                    isPriceCalc
                );
        }

        if (invFound != null) {
            BigDecimal totalInvoiceItem_ItemQtyInInvoice = calcItemQtyOutInInvoiceItems(invoiceId, createInvoiceItemDTO.getItemId(), true);
            if (
                !settings.isALLOW_NEGATIVE_INVENTORY() &&
                (
                    checkQty &&
                    storeService.checkNotItemQtyAvailable(
                        createInvoiceItemDTO.getItemId(),
                        ((createInvoiceItemDTO.getQty().multiply(invFound.getUnitPieces())).add(totalInvoiceItem_ItemQtyInInvoice))
                    )
                )
            ) {
                throw new ItemQtyException("مشكلة ف كمية الصنف", "لا يوجد ما يكفي من الصنف ف المخازن");
            }

            /**
             * add to invoice item found
             */
            AddQtyToInvoiceItem(invoiceOp.get(), invFound.getId(), createInvoiceItemDTO.getQty());
            regenerateInvoiceItemsPk(invoiceId);
            return findOne(invoiceId).orElseGet(null);
        } else {
            if (
                !settings.isALLOW_NEGATIVE_INVENTORY() &&
                checkQty &&
                storeService.checkNotItemQtyAvailable(createInvoiceItemDTO.getItemId(), createInvoiceItemDTO.getQty())
            ) {
                throw new ItemQtyException("مشكلة ف كمية الصنف", "لا يوجد ما يكفي من الصنف ف المخازن");
            }
        }

        BigDecimal qtyPieces = new BigDecimal(1);
        if (createInvoiceItemDTO.getUnitId() != null) {
            Optional<ItemUnit> itemUnitOp = itemService.getUnitItemById(createInvoiceItemDTO.getUnitId());
            if (itemUnitOp.isPresent()) {
                qtyPieces = itemUnitOp.get().getPieces();
            }
            /**unit**/
            if (
                checkQty &&
                storeService.checkNotItemQtyAvailable(createInvoiceItemDTO.getItemId(), createInvoiceItemDTO.getQty().add(qtyPieces))
            ) {
                throw new ItemQtyException("مشكلة ف كمية الصنف", "لا يوجد ما يكفي من الصنف ف المخازن");
            }
        }

        /**
         * create new Invoice item and add it to invoice
         */
        createInvoiceItem(invoiceOp.get(), createInvoiceItemDTO);
        regenerateInvoiceItemsPk(invoiceId);
        return findOne(invoiceId).orElseGet(null);
    }

    @Override
    public void deleteInvoiceItem(String id) {
        Optional<InvoiceItem> invoiceItem = invoiceItemRepository.findById(id);

        if (invoiceItem.isPresent()) {
            Optional<Invoice> invoice = findOneDomain(invoiceItem.get().getInvoiceId());
            if (invoice.isPresent()) {
                Invoice updatedInvoice = invoice.get();
                InvoiceItem itemToDelete = invoiceItem.get();

                // Calculate deleted quantity
                BigDecimal deletedQty = itemToDelete.getQtyOut() != null
                    ? itemToDelete.getQtyOut()
                    : (itemToDelete.getQtyIn() != null ? itemToDelete.getQtyIn() : BigDecimal.ZERO);

                // Add to deleted items history
                updatedInvoice.addDeletedItem(itemToDelete, deletedQty);

                // Log the deletion
                log.info(
                    "Removing invoice item: {} from invoice: {}. Item: {}, Quantity: {}, Unit: {}",
                    itemToDelete.getId(),
                    updatedInvoice.getId(),
                    itemToDelete.getItem().getName(),
                    deletedQty,
                    itemToDelete.getUnit()
                );

                // Remove item and update invoice
                updatedInvoice.getInvoiceItems().remove(itemToDelete);
                calcInvoice(invoiceRepository.save(updatedInvoice), true);
                invoiceItemRepository.delete(itemToDelete);
                regenerateInvoiceItemsPk(updatedInvoice.getId());
            }
        }
    }

    /**
     * Calculates the invoice details and optionally saves the invoice to the repository.
     *
     * @param invoice The invoice object to calculate details for
     * @param save A boolean flag indicating whether to save the invoice to the repository
     * @return The updated invoice object with calculated details, or the original invoice if not saved
     */
    @Override
    public Invoice calcInvoice(Invoice invoice, boolean save) {
        //return null;
        AggregateIterable<Document> result = this.mongoQueryService.calculateInvoiceQUERY(invoice.getId());

        MongoCursor<Document> iterator = result.iterator();

        if (iterator.hasNext()) {
            var tResult = iterator.next();
            Decimal128 totalPrice = Decimal128.parse(tResult.get("totalPrice").toString());
            Decimal128 totalCost = Decimal128.parse(tResult.get("totalCost").toString());
            Decimal128 totalNetPrice = Decimal128.parse(tResult.get("totalNetPrice").toString());
            Decimal128 totalNetCost = Decimal128.parse(tResult.get("totalNetCost").toString());

            invoice.setTotalPrice(totalPrice.bigDecimalValue());
            invoice.setTotalCost(totalCost.bigDecimalValue());
            invoice.setNetPrice(totalNetPrice.bigDecimalValue());
            invoice.setNetCost(totalNetCost.bigDecimalValue());

            calcInvoiceDiscount(invoice);
            addInvoiceAddititon(invoice);

            invoice.setNetResult(invoice.getNetPrice().subtract(invoice.getNetCost()));
        } else {
            invoice.setTotalPrice(new BigDecimal(0));
            invoice.setTotalCost(new BigDecimal(0));
            invoice.setNetPrice(new BigDecimal(0));
            invoice.setNetCost(new BigDecimal(0));
            invoice.setNetResult(new BigDecimal(0));
        }

        return save ? invoiceRepository.save(invoice) : invoice;
    }

    @Override
    public void regenerateInvoiceItemsPk(String invoiceId) {
        Optional<Invoice> invoiceOp = invoiceRepository.findById(invoiceId);

        /**
         * invoice
         */
        if (invoiceOp.isEmpty()) {
            throw new InvoiceNotFoundException(null);
        }

        Invoice invoice = invoiceOp.get();
        AtomicInteger c = new AtomicInteger();
        invoice
            .getInvoiceItems()
            .forEach(invoiceItem -> {
                invoiceItem.setPk(c.getAndIncrement() + "");

                invoiceItemRepository.save(invoiceItem);
            });
    }

    /**
     * Calculates the discounted invoice based on the provided invoice details.
     *
     * @param invoice The original invoice to calculate the discount for
     * @return The invoice with the discount applied
     */
    @Override
    public Invoice calcInvoiceDiscount(Invoice invoice) {
        BigDecimal totalAmount;
        BigDecimal netAmount;

        // Determine if it's a sale or purchase, and set the correct fields
        if (invoice.getKind().equals(InvoiceKind.SALE)) {
            totalAmount = invoice.getTotalPrice();
            netAmount = invoice.getNetPrice();
        } else if (invoice.getKind().equals(InvoiceKind.PURCHASE)) {
            totalAmount = invoice.getTotalCost();
            netAmount = invoice.getNetCost();
        } else {
            // If kind is not sale or purchase, return the invoice as is
            return invoice;
        }

        // Check if a discount amount is set and greater than 0
        if (invoice.getDiscount() != null && invoice.getDiscount().compareTo(BigDecimal.ZERO) > 0) {
            // Calculate the discount percentage
            BigDecimal discountPer = invoice.getDiscount().divide(totalAmount, 4, RoundingMode.CEILING).multiply(BigDecimal.valueOf(100));

            // If the discount amount is less than the total amount, apply it
            if (invoice.getDiscount().compareTo(totalAmount) < 0) {
                invoice.discountPer(discountPer.intValue());
                netAmount = totalAmount.subtract(invoice.getDiscount());
            } else {
                // If the discount is too large, reset it
                invoice.discountPer(0);
                invoice.discount(BigDecimal.ZERO);
                netAmount = totalAmount;
            }

            // Set the net amount based on the kind
            if (invoice.getKind().equals(InvoiceKind.SALE)) {
                invoice.netPrice(netAmount);
            } else {
                invoice.netCost(netAmount);
            }

            return invoice;
        }

        /*
        // Check if discount percentage is set and greater than 0
        if (invoice.getDiscountPer() != null && invoice.getDiscountPer() > 0) {
            // Calculate the discount amount
            BigDecimal discount = BigDecimal
                .valueOf(invoice.getDiscountPer())
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.CEILING)
                .multiply(totalAmount);

            // If the discount is less than the total amount, apply it
            if (discount.compareTo(totalAmount) < 0) {
                invoice.discount(discount);
                netAmount = totalAmount.subtract(discount);
            } else {
                // If the discount is too large, reset it
                invoice.discountPer(0);
                invoice.discount(BigDecimal.ZERO);
                netAmount = totalAmount;
            }

            // Set the net amount based on the kind
            if (invoice.getKind().equals(InvoiceKind.SALE)) {
                invoice.netPrice(netAmount);
            } else {
                invoice.netCost(netAmount);
            }



            return invoice;
        }
*/
        // No discount, return the invoice as is
        return invoice;
    }

    /**
     * Adds additional charges to the invoice if the additions are greater than zero.
     *
     * @param invoice The invoice to which additions are to be added
     * @return The updated invoice with additional charges added, or the original invoice if no additions
     */
    @Override
    public Invoice addInvoiceAddititon(Invoice invoice) {
        if (invoice.getAdditions() != null && invoice.getAdditions().compareTo(new BigDecimal(0)) > 0) {
            switch (invoice.getKind()) {
                case SALE:
                    {
                        return invoice.netPrice(invoice.getNetPrice().add(invoice.getAdditions()));
                    }
                case PURCHASE:
                    {
                        return invoice.netCost(invoice.getNetCost().add(invoice.getAdditions()));
                    }
                default:
                    {
                        return invoice.netPrice(invoice.getNetPrice().add(invoice.getAdditions()));
                    }
            }
        }
        return invoice;
    }

    /**
     * Retrieves the printable invoice object based on the provided invoice ID.
     *
     * @param id The ID of the invoice to retrieve
     * @return The printable invoice object
     * @throws InvoiceNotFoundException if the invoice with the given ID is not found
     */
    @Override
    public InvoicePrintDTO getPrintInvoiceObject(String id) {
        Optional<Invoice> invoiceOp = invoiceRepository.findById(id);

        /**
         * invoice
         */
        if (invoiceOp.isEmpty()) {
            throw new InvoiceNotFoundException(null);
        }

        InvoicePrintDTO invoicePrintDTO = new InvoicePrintDTO();
        invoicePrintDTO.setInvoice(getMapper().toDto(invoiceOp.get()));

        return invoicePrintDTO;
    }

    /**
     * Saves the invoice after checking if it is active and updating item quantities based on settings.
     *
     * @param invoice The invoice to be saved
     * @return The saved invoice
     */
    @Override
    public Invoice saveInvoice(Invoice invoice) {
        if (invoice.isActive()) {
            return invoice;
        }

        if (invoice.isDeferred() && invoice.getAccount() == null) {
            throw new InvoiceException("مشكلة فاتورة ", "يجب ان يكون هناك حساب ف الفاتورة المؤجلة");
        }
        Settings settings = settingService.getSettings();
        boolean saleUpdateItemQtyAfterSave = settings.isSALES_UPDATE_ITEM_QTY_AFTER_SAVE();

        boolean purchaseUpdateItemQtyAfterSave = settings.isPURCHASE_UPDATE_ITEM_QTY_AFTER_SAVE();

        /**
         * TODO Options getUpdateItemQtyAfterSave
         */

        if (invoice.getInvoiceItems() == null || invoice.getInvoiceItems().isEmpty()) {
            throw new InvoiceException("مشكلة فاتورة ", "لايوجد اصناف ف الفاتورة");
        }

        invoice.setActive(true);

        /**
         * TODO error invoice PK dublicated value not inserted
         */
        Pk invoicePk = pkService.generatePkEntity(PkKind.INVOICE);
        invoice.setPk(invoicePk.getValue().toString());

        /**
         * item's QTY
         */

        if (invoice.getBank() == null && settings.getMAIN_SELECTED_BANK_ID() != null && !settings.getMAIN_SELECTED_BANK_ID().isEmpty()) {
            bankService.findOneDomain(settings.getMAIN_SELECTED_BANK_ID()).ifPresent(invoice::setBank);
        }

        if (invoice.getStore() == null && settings.getMAIN_SELECTED_STORE_ID() != null && !settings.getMAIN_SELECTED_STORE_ID().isEmpty()) {
            storeService.findOneDomain(settings.getMAIN_SELECTED_STORE_ID()).ifPresent(invoice::setStore);
        }

        // Handle deferred invoice money creation
        if (invoice.isDeferred()) {
            AccountUser account = invoice.getAccount();
            if (invoice.getKind() == InvoiceKind.SALE) {
                accountUserService.subtractAccountBalance(account.getId(), invoice.getNetPrice());
            } else if (invoice.getKind() == InvoiceKind.PURCHASE) {
                accountUserService.addAccountBalance(account.getId(), invoice.getNetCost());
            }
        }

        switch (invoice.getKind()) {
            case SALE:
                {
                    if (saleUpdateItemQtyAfterSave) {
                        invoice
                            .getInvoiceItems()
                            .forEach(invoiceItem -> {
                                storeService.subtractItemQtyFromStores(invoiceItem.getItem().getId(), invoiceItem.getQtyOut());
                            });
                    }
                    break;
                }
            case PURCHASE:
                {
                    if (purchaseUpdateItemQtyAfterSave) {
                        invoice
                            .getInvoiceItems()
                            .forEach(invoiceItem -> {
                                storeService.addItemQtyToStores(invoiceItem.getItem().getId(), invoiceItem.getQtyIn(), null);
                            });
                    }
                    break;
                }
        }

        return invoiceRepository.save(invoice);
    }

    /**
     * Saves an invoice with the given invoice ID.
     *
     * @param invoiceId The ID of the invoice to save
     * @return The saved invoice
     * @throws InvoiceNotFoundException if the invoice with the given ID is not found
     */
    @Override
    public Invoice saveInvoice(String invoiceId) {
        Optional<Invoice> invoiceOptional = invoiceRepository.findById(invoiceId);
        if (invoiceOptional.isEmpty()) {
            throw new InvoiceNotFoundException("الفاتودة غير موجودة");
        }
        return this.saveInvoice(invoiceOptional.get());
    }

    /**
     * Finds an invoice item by the given invoice ID and item ID.
     *
     * @param invoiceId The ID of the invoice
     * @param itemId The ID of the item
     * @return The invoice item if found, null otherwise
     */
    @Override
    public InvoiceItem findInvoiceItemByItemId(String invoiceId, String itemId) {
        MongoCollection<Document> collection = mongoTemplate.getCollection("invoice_items");
        AggregateIterable<Document> result = collection.aggregate(
            Arrays.asList(new Document("$match", new Document("item.$id", new ObjectId(itemId)).append("invoice_id", invoiceId)))
        );

        MongoCursor<Document> iterator = result.iterator();

        if (iterator.hasNext()) {
            ObjectId InvoiceItemId = (ObjectId) iterator.next().get("_id");

            Optional<InvoiceItem> invoiceItem = invoiceItemRepository.findById(InvoiceItemId.toString());
            return invoiceItem.orElse(null);
        } else {
            return null;
        }
    }

    /**
     * Finds all invoice items for the given invoice and item.
     *
     * @param invoiceId The ID of the invoice
     * @param itemId The ID of the item
     * @return A list of invoice items
     */
    @Override
    public List<InvoiceItem> findInvoiceItemsByItemId(String invoiceId, String itemId) {
        MongoCollection<Document> collection = mongoTemplate.getCollection("invoice_items");
        AggregateIterable<Document> result = collection.aggregate(
            Arrays.asList(new Document("$match", new Document("item.$id", new ObjectId(itemId)).append("invoice_id", invoiceId)))
        );

        MongoCursor<Document> iterator = result.iterator();

        List<String> invoiceItemsIdList = new ArrayList<>();
        while (iterator.hasNext()) {
            ObjectId InvoiceItemId = (ObjectId) iterator.next().get("_id");
            invoiceItemsIdList.add(InvoiceItemId.toString());
        }
        return invoiceItemsIdList
            .stream()
            .map(invoiceItemRepository::findById)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    /**
     * Finds an invoice item by the given invoice ID, item ID, and money.
     *
     * @param invoiceId The ID of the invoice
     * @param itemId The ID of the item
     * @param money The money value
     * @param isPrice A boolean indicating whether the money value is a price or a cost
     * @return The invoice item if found, null otherwise
     */
    @Override
    public InvoiceItem findInvoiceItemByItemIdAndMoney(String invoiceId, String itemId, BigDecimal money, boolean isPrice) {
        MongoCollection<Document> collection = mongoTemplate.getCollection("invoice_items");
        String moneyFieldName = isPrice ? "price" : "cost";

        AggregateIterable<Document> result = collection.aggregate(
            Arrays.asList(
                new Document(
                    "$match",
                    new Document("invoice_id", invoiceId)
                        .append("item.$id", new ObjectId(itemId))
                        .append(moneyFieldName, Decimal128.parse(money.toString()).toString())
                )
            )
        );

        MongoCursor<Document> iterator = result.iterator();

        if (iterator.hasNext()) {
            ObjectId InvoiceItemId = (ObjectId) iterator.next().get("_id");

            Optional<InvoiceItem> invoiceItem = invoiceItemRepository.findById(InvoiceItemId.toString());
            return invoiceItem.orElse(null);
        } else {
            return null;
        }
    }

    /**
     * Finds an invoice item by the given invoice ID, item ID, and unit ID.
     *
     * @param invoiceId The ID of the invoice
     * @param itemId The ID of the item
     * @param unitId The ID of the unit
     * @return The invoice item if found, null otherwise
     */
    @Override
    public InvoiceItem findInvoiceItemByItemIdAndUnitId(String invoiceId, String itemId, String unitId) {
        MongoCollection<Document> collection = mongoTemplate.getCollection("invoice_items");
        AggregateIterable<Document> result = collection.aggregate(
            Arrays.asList(
                new Document(
                    "$match",
                    new Document("item.$id", new ObjectId(itemId))
                        .append("invoice_id", invoiceId)
                        .append("ItemUnit.$id", new ObjectId(unitId))
                )
            )
        );

        MongoCursor<Document> iterator = result.iterator();
        if (iterator.hasNext()) {
            ObjectId InvoiceItemId = (ObjectId) iterator.next().get("_id");

            Optional<InvoiceItem> invoiceItem = invoiceItemRepository.findById(InvoiceItemId.toString());
            return invoiceItem.orElse(null);
        } else {
            return null;
        }
    }

    /**
     * Finds an invoice item by the given invoice ID, item ID, unit ID, and money value.
     *
     * @param invoiceId The ID of the invoice
     * @param itemId The ID of the item
     * @param unitId The ID of the unit
     * @param money The money value
     * @param isPrice A boolean indicating whether the money value is a price or a cost
     * @return The invoice item if found, null otherwise
     */
    @Override
    public InvoiceItem findInvoiceItemByItemIdAndUnitIdAndMoney(
        String invoiceId,
        String itemId,
        String unitId,
        BigDecimal money,
        boolean isPrice
    ) {
        MongoCollection<Document> collection = mongoTemplate.getCollection("invoice_items");

        String moneyFieldName = isPrice ? "price" : "cost";

        /**
         * [{
         *     $match: {
         *         $and: [{
         *                 invoice_id: '63433dd811fa063c76d7337e'
         *             },
         *             {
         *                 $or: [{
         *                         'item.$id': ObjectId('633eadc729392c32b279597e'),
         *                         'ItemUnit.$id': ObjectId('633eadcb29392c32b279597f'),
         *                         price: '0.0'
         *                     },
         *                     {
         *                         'item.$id': ObjectId('633eadc729392c32b279597e'),
         *                         'ItemUnit.$id': ObjectId('633eadcb29392c32b279597f')
         *                     }
         *                 ]
         *             }
         *         ]
         *     }
         * }]
         */

        AggregateIterable<Document> result = collection.aggregate(
            Arrays.asList(
                new Document(
                    "$match",
                    new Document("invoice_id", invoiceId)
                        .append("item.$id", new ObjectId(itemId))
                        .append("ItemUnit.$id", new ObjectId(unitId))
                        .append(moneyFieldName, Decimal128.parse(money.toString()).toString())
                )
            )
        );

        MongoCursor<Document> iterator = result.iterator();
        if (iterator.hasNext()) {
            ObjectId InvoiceItemId = (ObjectId) iterator.next().get("_id");

            Optional<InvoiceItem> invoiceItem = invoiceItemRepository.findById(InvoiceItemId.toString());
            return invoiceItem.orElse(null);
        } else {
            return null;
        }
    }

    /**
     * Adds qty to invoice item
     *
     * @param invoice  selected invoice
     * @param invoiceItemId selected invoice item id
     * @param qty qty to add
     * @return saved or modified {@link InvoiceItem} object
     */
    @Override
    public InvoiceItem AddQtyToInvoiceItem(Invoice invoice, String invoiceItemId, BigDecimal qty) {
        Optional<InvoiceItem> invoiceItem = invoiceItemRepository.findById(invoiceItemId);
        if (invoiceItem.isPresent()) {
            InvoiceItem invFound = invoiceItem.get();

            switch (invoice.getKind()) {
                case SALE:
                    {
                        InvoiceItem invoiceItem1 = this.saleService.AddQtyToInvoiceItem(invFound, qty, true);
                        invoiceItem1 = invoiceItemRepository.save(invoiceItem1);
                        this.calcInvoice(invoice, true);
                        return invoiceItem1;
                    }
                case PURCHASE:
                    {
                        InvoiceItem invoiceItem1 = this.purchaseService.AddQtyToInvoiceItem(invFound, qty, false);
                        invoiceItem1 = invoiceItemRepository.save(invoiceItem1);
                        this.calcInvoice(invoice, true);
                        return invoiceItem1;
                    }
            }
        }
        return null;
    }

    /**
     * @param invoiceItem
     * @return
     */
    @Override
    public InvoiceItem setInvoiceItemNullUnit(InvoiceItem invoiceItem) {
        invoiceItem.setUnitPieces(new BigDecimal(1));
        invoiceItem.setUnitPrice(invoiceItem.getPrice());
        invoiceItem.setUnitCost(invoiceItem.getCost());
        invoiceItem.setItemUnit(null);
        invoiceItem.setUnit("-");
        return invoiceItem;
    }

    /**
     * Updates an invoice item partially based on the provided InvoiceItemUpdateDTO.
     *
     * @param id The ID of the invoice item to update
     * @param invoiceItemUpdateDTO The DTO containing the updated information for the invoice item
     * @return The updated invoice item entity
     */
    @Override
    public InvoiceItemViewDTO updateInvoiceItem(String id, InvoiceItemUpdateDTO invoiceItemUpdateDTO) {
        Settings settings = settingService.getSettings();
        return invoiceItemRepository
            .findById(id)
            .map(existingInvoiceItem -> {
                // Calculate difference in quantity
                if (settings.isSAVE_INVOICE_DELETETED_INVOICEITEMS()) {
                    BigDecimal oldQty = existingInvoiceItem.getUserQty();
                    BigDecimal newQty = invoiceItemUpdateDTO.getQty();

                    if (oldQty != null && newQty != null && oldQty.compareTo(newQty) > 0) {
                        // If new quantity is less than old quantity, track the difference
                        BigDecimal deletedQty = oldQty.subtract(newQty);

                        // Update invoice's deletedItems
                        Optional<Invoice> invoice = findOneDomain(existingInvoiceItem.getInvoiceId());
                        if (invoice.isPresent()) {
                            Invoice updatedInvoice = invoice.get();

                            // Add to deleted items history
                            updatedInvoice.addDeletedItem(existingInvoiceItem, deletedQty);

                            // Log the update
                            log.info(
                                "Updating invoice item: {} in invoice: {}. Item: {}, Reduced quantity: {}, Unit: {}",
                                existingInvoiceItem.getId(),
                                updatedInvoice.getId(),
                                existingInvoiceItem.getItem().getName(),
                                deletedQty,
                                existingInvoiceItem.getUnit()
                            );

                            invoiceRepository.save(updatedInvoice);
                        }
                    }
                }
                existingInvoiceItem.setUserQty(invoiceItemUpdateDTO.getQty());
                invoiceItemMapper.partialUpdate(existingInvoiceItem, invoiceItemUpdateDTO);
                calcInvoiceInvoiceItem(existingInvoiceItem);
                calcInvoice(findOneDomain(existingInvoiceItem.getInvoiceId()).get(), true);
                return existingInvoiceItem;
            })
            .map(invoiceItemMapper::toInvoiceItemViewDTO)
            .orElseGet(null);
    }

    /**
     * Removes all temp and not activated invoices that are older than 3 days.
     */
    @Override
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeTempInvoices() {
        log.info("Removing all temp and not activated invoices", new Date().getTime());
        invoiceRepository
            .findAllByActiveIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(tempNotActiveInvoice -> {
                this.delete(tempNotActiveInvoice.getId());
            });
    }

    /**
     * Searches for the Invoice corresponding to the query parameters.
     *
     * @param invoicesSearchModel the search model containing query parameters
     * @return a container of InvoiceViewDTO objects matching the query parameters
     */
    @Override
    public InvoiceViewDTOContainer invoicesViewSearch(InvoicesSearchModel invoicesSearchModel) {
        return this.mongoQueryService.searchInvoicesQUERY(invoicesSearchModel);
    }

    /**
     * get invoice's Invoice items list
     *
     * @param id invoice id
     * @return list of invoice invoiceitems
     */
    @Override
    public List<InvoiceItemDTO> getInvoiceItems(String id) {
        Optional<Invoice> invoiceItem = findOneDomain(id);
        if (invoiceItem.isEmpty()) {
            throw new InvoiceNotFoundException(null);
        }
        return invoiceItem.get().getInvoiceItems().stream().map(invoiceItemMapper::toDto).collect(Collectors.toList());
    }
}
