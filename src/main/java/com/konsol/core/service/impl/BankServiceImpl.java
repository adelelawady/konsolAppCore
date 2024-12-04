package com.konsol.core.service.impl;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import com.konsol.core.domain.Bank;
import com.konsol.core.repository.BankRepository;
import com.konsol.core.repository.InvoiceRepository;
import com.konsol.core.repository.MoneyRepository;
import com.konsol.core.service.BankService;
import com.konsol.core.service.api.dto.BankDTO;
import com.konsol.core.service.dto.BankBalanceDTO;
import com.konsol.core.service.dto.BankMovementDTO;
import com.konsol.core.service.exception.BankNotFoundException;
import com.konsol.core.service.mapper.BankMapper;
import com.mongodb.MongoException;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.bson.BsonNull;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link Bank}.
 */
@Service
public class BankServiceImpl implements BankService {

    private final Logger log = LoggerFactory.getLogger(BankServiceImpl.class);

    private final BankRepository bankRepository;

    private final BankMapper bankMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private MoneyRepository moneyRepository;

    @Autowired
    private InvoiceRepository invoiceRepository;

    public BankServiceImpl(BankRepository bankRepository, BankMapper bankMapper) {
        this.bankRepository = bankRepository;
        this.bankMapper = bankMapper;
    }

    @Override
    public BankDTO save(BankDTO bankDTO) {
        log.debug("Request to save Bank : {}", bankDTO);
        Bank bank = bankMapper.toEntity(bankDTO);
        bank = bankRepository.save(bank);
        return bankMapper.toDto(bank);
    }

    @Override
    public Optional<BankDTO> update(BankDTO bankDTO) {
        log.debug("Request to partially update Bank : {}", bankDTO);

        return bankRepository
            .findById(bankDTO.getId())
            .map(existingBank -> {
                bankMapper.partialUpdate(existingBank, bankDTO);

                return existingBank;
            })
            .map(bankRepository::save)
            .map(bankMapper::toDto);
    }

    @Override
    public Page<BankDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Banks");
        return bankRepository.findAll(pageable).map(bankMapper::toDto);
    }

    @Override
    public Optional<BankDTO> findOne(String id) {
        log.debug("Request to get Bank : {}", id);
        return bankRepository.findById(id).map(bankMapper::toDto);
    }

    @Override
    public Optional<Bank> findOneDomain(String id) {
        log.debug("Request to get Bank : {}", id);
        return bankRepository.findById(id);
    }

    @Override
    public void delete(String id) {
        log.debug("Request to delete Bank : {}", id);
        bankRepository.deleteById(id);
    }

    @Override
    public Optional<Bank> findFirstByOrderById() {
        return bankRepository.findFirstByOrderById();
    }

    @Override
    public List<BankMovementDTO> loadBankMovements(String bankId) {
        log.debug("Request to load bank movements for bank ID: {}", bankId);

        // Validate bank exists
        Optional<Bank> bank = bankRepository.findById(bankId);
        if (!bank.isPresent()) {
            log.error("Bank not found with ID: {}", bankId);
            throw new BankNotFoundException(String.format("Bank with id %s not found", bankId));
        }

        try {
            List<BankMovementDTO> movements = new ArrayList<>();

            // Create pipeline for Money collection
            List<AggregationOperation> moneyOperations = Arrays.asList(
                // Match documents for the specific bank
                match(Criteria.where("bank.$id").is(new ObjectId(bankId))),
                // Project the required fields
                project()
                    .and("_id")
                    .as("id")
                    .and("pk")
                    .as("pk")
                    .andExpression("{ $toString: '$bank.$id' }")
                    .as("bankId")
                    .andExpression("'MONEY'")
                    .as("sourceType")
                    .and(ConditionalOperators.ifNull("kind").then("UNKNOWN"))
                    .as("sourceKind")
                    .and("_id")
                    .as("sourceId")
                    .and("pk")
                    .as("sourcePk")
                    .and(ConditionalOperators.ifNull("money_in").then(0))
                    .as("moneyIn")
                    .and(ConditionalOperators.ifNull("money_out").then(0))
                    .as("moneyOut")
                    .and("details")
                    .as("details")
                    .and("created_date")
                    .as("createdDate")
                    .andExpression("{ $toString: '$account.$id' }")
                    .as("accountId")
            );

            // Create pipeline for Invoice collection
            List<AggregationOperation> invoiceOperations = Arrays.asList(
                // Match documents for the specific bank
                match(Criteria.where("bank.$id").is(new ObjectId(bankId))),
                // Project the required fields with conditional money in/out based on invoice kind
                project()
                    .and("_id")
                    .as("id")
                    .and("pk")
                    .as("pk")
                    .andExpression("{ $toString: '$bank.$id' }")
                    .as("bankId")
                    .andExpression("'INVOICE'")
                    .as("sourceType")
                    .and(ConditionalOperators.ifNull("kind").then("UNKNOWN"))
                    .as("sourceKind")
                    .and("_id")
                    .as("sourceId")
                    .and("pk")
                    .as("sourcePk")
                    .and(ConditionalOperators.ifNull("net_price").then(0))
                    .as("moneyIn")
                    .and(ConditionalOperators.ifNull("net_cost").then(0))
                    .as("moneyOut")
                    .and("details")
                    .as("details")
                    .and("created_date")
                    .as("createdDate")
                    .andExpression("{ $toString: '$account.$id' }")
                    .as("accountId")
            );

            // Execute Money aggregation
            movements.addAll(mongoTemplate.aggregate(newAggregation(moneyOperations), "monies", BankMovementDTO.class).getMappedResults());

            // Execute Invoice aggregation
            movements.addAll(
                mongoTemplate.aggregate(newAggregation(invoiceOperations), "invoices", BankMovementDTO.class).getMappedResults()
            );

            // Sort the combined results by createdDate
            movements.sort((m1, m2) -> {
                if (m1.getCreatedDate() == null && m2.getCreatedDate() == null) return 0;
                if (m1.getCreatedDate() == null) return 1;
                if (m2.getCreatedDate() == null) return -1;
                return m2.getCreatedDate().compareTo(m1.getCreatedDate());
            });

            log.debug("Successfully loaded {} bank movements for bank ID: {}", movements.size(), bankId);

            log.debug("movements size  {} movements: {}", movements.size(), movements.toString());
            return movements;
        } catch (Exception e) {
            log.error("Error loading bank movements for bank ID {}: {}", bankId, e.getMessage());
            throw new RuntimeException("Error loading bank movements", e);
        }
    }

    @Override
    public BankBalanceDTO calculateBankBalance(String bankId) {
        if (bankId == null || bankId.trim().isEmpty()) {
            throw new IllegalArgumentException("Bank ID cannot be null or empty");
        }

        try {
            // Aggregation for invoices
            List<Document> invoicePipeline =
                (
                    Arrays.asList(
                        new Document("$match", new Document("bank.$id", new ObjectId(bankId))),
                        new Document(
                            "$project",
                            new Document(
                                "salesTotalPrice",
                                new Document(
                                    "$cond",
                                    new Document("if", new Document("$eq", Arrays.asList("$kind", "SALE")))
                                        .append("then", new Document("$toDouble", "$total_price"))
                                        .append("else", 0L)
                                )
                            )
                                .append(
                                    "salesNetPrice",
                                    new Document(
                                        "$cond",
                                        new Document("if", new Document("$eq", Arrays.asList("$kind", "SALE")))
                                            .append("then", new Document("$toDouble", "$net_price"))
                                            .append("else", 0L)
                                    )
                                )
                                .append(
                                    "purchaseTotalCost",
                                    new Document(
                                        "$cond",
                                        new Document("if", new Document("$eq", Arrays.asList("$kind", "PURCHASE")))
                                            .append("then", new Document("$toDouble", "$total_cost"))
                                            .append("else", 0L)
                                    )
                                )
                                .append(
                                    "purchaseNetCost",
                                    new Document(
                                        "$cond",
                                        new Document("if", new Document("$eq", Arrays.asList("$kind", "PURCHASE")))
                                            .append("then", new Document("$toDouble", "$net_cost"))
                                            .append("else", 0L)
                                    )
                                )
                                .append(
                                    "discount",
                                    new Document(
                                        "$subtract",
                                        Arrays.asList(new Document("$toDouble", "$total_price"), new Document("$toDouble", "$net_price"))
                                    )
                                )
                                .append("additions", new Document("$toDouble", "$additions"))
                        ),
                        new Document(
                            "$group",
                            new Document("_id", new BsonNull())
                                .append("totalSalesTotalPrice", new Document("$sum", "$salesTotalPrice"))
                                .append("totalSalesNetPrice", new Document("$sum", "$salesNetPrice"))
                                .append("totalPurchasesTotalCost", new Document("$sum", "$purchaseTotalCost"))
                                .append("totalPurchasesNetCost", new Document("$sum", "$purchaseNetCost"))
                                .append("totalDiscounts", new Document("$sum", "$discount"))
                                .append("totalAdditions", new Document("$sum", "$additions"))
                        ),
                        new Document(
                            "$project",
                            new Document(
                                "totalSalesProfits",
                                new Document("$subtract", Arrays.asList("$totalSalesNetPrice", "$totalPurchasesNetCost"))
                            )
                                .append("totalDiscounts", 1L)
                                .append("totalAdditions", 1L)
                                .append("grossRevenue", "$totalSalesTotalPrice")
                                .append("netRevenue", "$totalSalesNetPrice")
                                .append("totalCost", "$totalPurchasesNetCost")
                        )
                    )
                );

            // Execute invoice aggregation
            MongoCollection<Document> invoiceCollection = mongoTemplate.getCollection("invoices");
            AggregateIterable<Document> invoiceResult = invoiceCollection.aggregate(invoicePipeline);
            Document invoiceAggregation = invoiceResult.first();

            if (invoiceAggregation == null) {
                log.warn("No invoice data found for bank ID: {}", bankId);
                return new BankBalanceDTO(); // Return empty DTO if no result
            }

            // Aggregation for monies
            List<Document> moneyPipeline = Arrays.asList(
                new Document("$match", new Document("bank.$id", new ObjectId(bankId))),
                new Document(
                    "$group",
                    new Document("_id", new BsonNull())
                        .append("totalMoneyIn", new Document("$sum", new Document("$toDouble", "$money_in")))
                        .append("totalMoneyOut", new Document("$sum", new Document("$toDouble", "$money_out")))
                )
            );

            //   Execute money aggregation
            MongoCollection<Document> moneyCollection = mongoTemplate.getCollection("monies");
            AggregateIterable<Document> moneyResult = moneyCollection.aggregate(moneyPipeline);
            Document moneyAggregation = moneyResult.first();

            log.info("Aggregation Result: {}", invoiceAggregation);
            log.info("Money Aggregation Result: {}", moneyAggregation);
            // Map results to BankBalanceDTO
            BankBalanceDTO bankBalance = new BankBalanceDTO();

            // Set invoice-related values
            bankBalance.setGrossRevenue(safeGetDouble(invoiceAggregation, "grossRevenue"));
            bankBalance.setNetRevenue(safeGetDouble(invoiceAggregation, "netRevenue"));
            bankBalance.setTotalCost(safeGetDouble(invoiceAggregation, "totalCost"));
            bankBalance.setTotalSalesProfits(safeGetDouble(invoiceAggregation, "totalSalesProfits"));
            bankBalance.setTotalDiscounts(safeGetDouble(invoiceAggregation, "totalDiscounts"));
            bankBalance.setTotalAdditions(safeGetDouble(invoiceAggregation, "totalAdditions"));

            // Set money-related values
            bankBalance.setMoneyInFromMoney(safeGetDouble(moneyAggregation, "totalMoneyIn"));
            bankBalance.setMoneyOutFromMoney(safeGetDouble(moneyAggregation, "totalMoneyOut"));

            // Calculate derived fields
            double totalMoneyIn = bankBalance.getMoneyInFromMoney();
            double totalMoneyOut = bankBalance.getMoneyOutFromMoney();
            // double totalBalance = totalMoneyIn - totalMoneyOut;

            double totalBalance =
                (totalMoneyIn - totalMoneyOut) +
                bankBalance.getTotalSalesProfits() +
                bankBalance.getTotalAdditions() -
                bankBalance.getTotalDiscounts();

            bankBalance.setTotalBalance(totalBalance);

            // Log the results
            log.info("Bank Balance DTO: {}", bankBalance);

            return bankBalance;
        } catch (Exception e) {
            log.error("Error calculating bank balance for bank ID: {}", bankId, e);
            throw new RuntimeException("Failed to calculate bank balance", e);
        }
    }

    private double safeGetDouble(Document doc, String key) {
        if (doc == null || !doc.containsKey(key)) {
            log.warn("Field {} not found in the document.", key);
            return 0.0;
        }

        Object value = doc.get(key);
        if (value == null) {
            log.warn("Null value for key: {}", key);
            return 0.0;
        }

        try {
            if (value instanceof Number) {
                return ((Number) value).doubleValue();
            }

            if (value instanceof String) {
                return Double.parseDouble((String) value);
            }
        } catch (NumberFormatException e) {
            log.warn("Unable to parse value for key {}: {}", key, value);
        }

        return 0.0;
    }
}
/*


أمثلة لسيناريوهات الحسابات
1. حساب بسيط يعتمد على الأموال فقط
إذا كانت الخزينة تعتمد فقط على الإيداعات والسحوبات:
makefile
Copy code
totalBalance = totalMoneyIn - totalMoneyOut
2. حساب يشمل الفواتير
إذا كانت الفواتير (المبيعات والمشتريات) تؤثر على الخزينة:
scss
Copy code
totalBalance = (totalMoneyIn - totalMoneyOut) + (netRevenue - totalCost)
3. حساب يشمل الإضافات والخصومات
إذا كانت هناك خصومات أو إضافات تؤثر على الإيرادات:
scss
Copy code
totalBalance = (totalMoneyIn - totalMoneyOut) + (netRevenue - totalCost) + totalAdditions - totalDiscounts
4. حساب يشمل الأرباح والمصادر الأخرى
إذا كنت تريد حساب كل العوامل التي تؤثر على الخزينة:
makefile
Copy code
totalBalance = (totalMoneyIn - totalMoneyOut) + totalSalesProfits + totalAdditions - totalDiscounts



 */
