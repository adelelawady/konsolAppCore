package com.konsol.core.service.impl;

import com.konsol.core.domain.*;
import com.konsol.core.domain.enumeration.InvoiceKind;
import com.konsol.core.domain.enumeration.PkKind;
import com.konsol.core.repository.InvoiceItemRepository;
import com.konsol.core.repository.InvoiceRepository;
import com.konsol.core.service.*;
import com.konsol.core.service.api.dto.*;
import com.konsol.core.service.mapper.InvoiceItemMapper;
import com.konsol.core.service.mapper.InvoiceMapper;
import com.konsol.core.web.rest.api.SystemResource;
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

    protected final InvoiceItemRepository invoiceItemRepository;

    private final MongoQueryService mongoQueryService;
    private final InvoiceItemMapper invoiceItemMapper;

    @Qualifier(value = "SALES")
    private final SaleService saleService;

    @Qualifier(value = "PURCHASE")
    private final PurchaseService purchaseService;

    private final MongoTemplate mongoTemplate;

    private SystemConfiguration systemConfiguration;

    private final SystemResource systemResource;

    public InvoiceServiceImpl(
        InvoiceRepository invoiceRepository,
        InvoiceMapper invoiceMapper,
        PkService pkService,
        ItemService itemService,
        StoreService storeService,
        InvoiceItemRepository invoiceItemRepository,
        MongoQueryService mongoQueryService,
        InvoiceItemMapper invoiceItemMapper,
        SaleService saleService,
        PurchaseService purchaseService,
        MongoTemplate mongoTemplate,
        SystemResource systemResource
    ) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.pkService = pkService;
        this.itemService = itemService;
        this.storeService = storeService;
        this.invoiceItemRepository = invoiceItemRepository;
        this.mongoQueryService = mongoQueryService;
        this.invoiceItemMapper = invoiceItemMapper;
        this.saleService = saleService;
        this.purchaseService = purchaseService;
        this.mongoTemplate = mongoTemplate;
        this.systemResource = systemResource;
        systemConfiguration = systemResource.getSystemConfigurations();
    }

    @Override
    public Invoice updateInvoice(InvoiceUpdateDTO invoiceUpdateDTO) {
        log.debug("Request to partially update Invoice : {}", invoiceUpdateDTO);

        /**
         * ignore invoice items
         */
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

    public Page<InvoiceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return invoiceRepository.findAllWithEagerRelationships(pageable).map(invoiceMapper::toDto);
    }

    @Override
    public Optional<InvoiceDTO> findOne(String id) {
        log.debug("Request to get Invoice : {}", id);
        return invoiceRepository.findOneWithEagerRelationships(id).map(invoiceMapper::toDto);
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

    @Override
    public InvoiceDTO initializeNewInvoice(InvoiceKind kind) {
        Invoice invoice = new Invoice();
        invoice.setTemp(true);
        invoice.setActive(false);
        invoice.setPk(null);
        invoice.setKind(kind);
        Invoice resultInvoice = invoiceRepository.save(invoice);
        Pk pk = pkService.getPkEntity(PkKind.INVOICE);
        resultInvoice.setPk(pk.getValue().add(new BigDecimal(1)).intValue() + "");
        return invoiceMapper.toDto(resultInvoice);
    }

    /**
     * @param invoiceItem
     * @param unitId
     * @return
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

    @Override
    public InvoiceItem initializeNewInvoiceItem(InvoiceKind kind, String ItemId, String unitId, BigDecimal userQty, BigDecimal userPrice) {
        Optional<Item> itemOp = itemService.findOneById(ItemId);
        InvoiceItem invoiceItem = new InvoiceItem();
        /**
         * item
         */
        if (!itemOp.isPresent()) {
            throw new ItemNotFoundException(null, null);
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

        return this.invoiceItemRepository.save(invoiceItem);
    }

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

    @Override
    public InvoiceDTO addInvoiceItem(String invoiceId, CreateInvoiceItemDTO createInvoiceItemDTO) {
        Optional<Invoice> invoiceOp = invoiceRepository.findById(invoiceId);

        // Optional<Item> itemOp = itemService.findOneById(createInvoiceItemDTO.getItemId());

        /**
         * invoice
         */
        if (!invoiceOp.isPresent()) {
            throw new InvoiceNotFoundException(null, null);
        }

        boolean isPriceCalc = invoiceOp.get().getKind().equals(InvoiceKind.SALE);
        boolean isCostCalc = invoiceOp.get().getKind().equals(InvoiceKind.PURCHASE);

        boolean checkQty =
            invoiceOp.get().getKind().equals(InvoiceKind.SALE) &&
            systemConfiguration.getSysOptions().getSettings().getSalesInvoiceOptions().getCheckItemQty();

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
            BigDecimal totalInvoiceItemitemQtyInInvoice = calcItemQtyOutInInvoiceItems(invoiceId, createInvoiceItemDTO.getItemId(), true);
            if (
                checkQty &&
                !storeService.checkItemQtyAvailable(
                    createInvoiceItemDTO.getItemId(),
                    ((createInvoiceItemDTO.getQty().multiply(invFound.getUnitPieces())).add(totalInvoiceItemitemQtyInInvoice))
                )
            ) {
                throw new ItemQtyException("مشكلة ف كمية الصنف", "لا يوجد ما يكفي من الصنف ف المخازن", null);
            }

            /**
             * add to invoice item found
             */
            AddQtyToInvoiceItem(invoiceOp.get(), invFound.getId(), createInvoiceItemDTO.getQty());
            regenerateInvoiceItemsPk(invoiceId);
            return findOne(invoiceId).orElseGet(null);
        }

        BigDecimal qtyPieces = new BigDecimal(1);
        if (createInvoiceItemDTO.getUnitId() != null) {
            Optional<ItemUnit> itemUnitOp = itemService.getUnitItemById(createInvoiceItemDTO.getUnitId());
            if (itemUnitOp.isPresent()) {
                qtyPieces = itemUnitOp.get().getPieces();
            }
        }

        /**unit**/
        if (
            checkQty && !storeService.checkItemQtyAvailable(createInvoiceItemDTO.getItemId(), createInvoiceItemDTO.getQty().add(qtyPieces))
        ) {
            throw new ItemQtyException("مشكلة ف كمية الصنف", "لا يوجد ما يكفي من الصنف ف المخازن", null);
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
                updatedInvoice.getInvoiceItems().remove(invoiceItem.get());

                calcInvoice(invoiceRepository.save(updatedInvoice), true);
                invoiceItemRepository.delete(invoiceItem.get());
                regenerateInvoiceItemsPk(updatedInvoice.getId());
            }
        }
    }

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
            //invoice.setDiscount(new BigDecimal(0));
            //invoice.setDiscountPer(0);
        }

        return save ? invoiceRepository.save(invoice) : invoice;
    }

    @Override
    public void regenerateInvoiceItemsPk(String invoiceId) {
        Optional<Invoice> invoiceOp = invoiceRepository.findById(invoiceId);

        /**
         * invoice
         */
        if (!invoiceOp.isPresent()) {
            throw new InvoiceNotFoundException(null, null);
        }

        Invoice invoice = invoiceOp.get();
        AtomicInteger c = new AtomicInteger();
        invoice
            .getInvoiceItems()
            .stream()
            .forEach(invoiceItem -> {
                invoiceItem.setPk(c.getAndIncrement() + "");

                invoiceItemRepository.save(invoiceItem);
            });
    }

    @Override
    public Invoice calcInvoiceDiscount(Invoice invoice) {
        if (!(invoice.getDiscountPer() == null || (invoice.getDiscountPer() == 0))) {
            BigDecimal discount =
                (BigDecimal.valueOf(invoice.getDiscountPer()).divide(BigDecimal.valueOf(100), 4, RoundingMode.CEILING)).multiply(
                        invoice.getTotalPrice()
                    );

            if (discount.compareTo(invoice.getTotalPrice()) < 0) {
                invoice.discount(discount);
                invoice.netPrice(invoice.getTotalPrice().subtract(discount));
            } else {
                invoice.discountPer(0);
                invoice.discount(new BigDecimal(0));
                invoice.netPrice(invoice.getTotalPrice());
            }
            return invoice;
        }

        if (!(invoice.getDiscount() == null || (invoice.getDiscount().compareTo(new BigDecimal(0))) == 0)) {
            BigDecimal discountPer =
                (invoice.getDiscount().divide(invoice.getTotalPrice(), 4, RoundingMode.CEILING)).multiply(BigDecimal.valueOf(100));
            if (invoice.getDiscount().compareTo(invoice.getTotalPrice()) == -1) {
                return invoice.discountPer(discountPer.intValue()).netPrice(invoice.getTotalPrice().subtract(invoice.getDiscount()));
            } else {
                return invoice.discountPer(0).discount(new BigDecimal(0)).netPrice(invoice.getTotalPrice());
            }
        }
        return invoice;
    }

    @Override
    public Invoice addInvoiceAddititon(Invoice invoice) {
        if (invoice.getAdditions() != null && invoice.getAdditions().compareTo(new BigDecimal(0)) == 1) {
            return invoice.netPrice(invoice.getNetPrice().add(invoice.getAdditions()));
        }
        return invoice;
    }

    @Override
    public InvoicePrintDTO getPrintInvoiceObject(String id) {
        Optional<Invoice> invoiceOp = invoiceRepository.findById(id);

        /**
         * invoice
         */
        if (!invoiceOp.isPresent()) {
            throw new InvoiceNotFoundException(null, null);
        }

        InvoicePrintDTO invoicePrintDTO = new InvoicePrintDTO();
        invoicePrintDTO.setInvoice(getMapper().toDto(invoiceOp.get()));

        SystemConfiguration systemConfiguration = systemResource.getSystemConfigurations();

        invoicePrintDTO.globalInfo(systemConfiguration.getSysOptions().getContactInfo());

        return invoicePrintDTO;
    }

    @Override
    public Invoice saveInvoice(Invoice invoice) {
        if (invoice.isActive()) {
            return invoice;
        }

        boolean updateItemQtyAfterSave = systemConfiguration
            .getSysOptions()
            .getSettings()
            .getSalesInvoiceOptions()
            .getUpdateItemQtyAfterSave();

        if (invoice.getInvoiceItems() == null || invoice.getInvoiceItems().isEmpty()) {
            throw new InvoiceException("مشكلة فاتورة ", "لايوجد اصناف ف الفاتورة", null);
        }

        invoice.setActive(true);
        invoice.setTemp(false);

        /**
         * TODO error invoice PK dublicated value not inserted
         */
        Pk invoicePk = pkService.generatePkEntity(PkKind.INVOICE);
        invoice.setPk(invoicePk.getValue().toString());

        /**
         * TODO handle bank on invoice saving
         */

        /**
         * TODO handle Store on invoice saving
         */

        /**
         * TODO handle account on invoice saving
         */

        /**
         * item's QTY
         */

        if (updateItemQtyAfterSave) {
            switch (invoice.getKind()) {
                case SALE:
                    {
                        // TODO selected store to subtract from
                        invoice
                            .getInvoiceItems()
                            .stream()
                            .forEach(invoiceItem -> {
                                storeService.subtractItemQtyFromStores(invoiceItem.getItem().getId(), invoiceItem.getQtyOut());
                            });
                        break;
                    }
                case PURCHASE:
                    {
                        // TODO selected store to add to

                        invoice
                            .getInvoiceItems()
                            .stream()
                            .forEach(invoiceItem -> {
                                storeService.addItemQtyToStores(invoiceItem.getItem().getId(), invoiceItem.getQtyIn(), null);
                            });
                        break;
                    }
            }
        }

        Thread thread = new Thread(() -> {
            systemConfiguration = systemResource.getSystemConfigurations();
        });
        thread.start();

        return invoiceRepository.save(invoice);
    }

    /**
     * @param invoiceId
     * @return
     */
    @Override
    public Invoice saveInvoice(String invoiceId) {
        Optional<Invoice> invoiceOptional = invoiceRepository.findById(invoiceId);
        if (!invoiceOptional.isPresent()) {
            throw new InvoiceNotFoundException("الفاتودة غير موجودة", null);
        }
        return this.saveInvoice(invoiceOptional.get());
    }

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

    @Override
    public InvoiceItemViewDTO updateInvoiceItem(String id, InvoiceItemUpdateDTO invoiceItemUpdateDTO) {
        return invoiceItemRepository
            .findById(id)
            .map(existingInvoiceItem -> {
                invoiceItemMapper.partialUpdate(existingInvoiceItem, invoiceItemUpdateDTO);
                calcInvoiceInvoiceItem(existingInvoiceItem);
                calcInvoice(findOneDomain(existingInvoiceItem.getInvoiceId()).get(), true);
                return existingInvoiceItem;
            })
            //.map(invoiceItemRepository::save)
            .map(invoiceItemMapper::toInvoiceItemViewDTO)
            .orElseGet(null);
    }

    @Override
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeTempInvoices() {
        log.info("Removing all temp and not activated invoices", new Date().getTime());
        invoiceRepository
            .findAllByActiveIsFalseAndTempIsTrueAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(tempNotActiveInvoice -> {
                this.delete(tempNotActiveInvoice.getId());
            });
    }

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
        if (!invoiceItem.isPresent()) {
            throw new InvoiceNotFoundException(null, null);
        }
        return invoiceItem.get().getInvoiceItems().stream().map(invoiceItemMapper::toDto).collect(Collectors.toList());
    }
}
