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

    public BankBalanceDTO calculateBankBalance(String bankId) {
        if (bankId == null || bankId.trim().isEmpty()) {
            throw new IllegalArgumentException("Bank ID cannot be null or empty");
        }

        try {
            // Create the aggregation pipeline
            List<Document> pipeline = Arrays.asList(
                // Match invoices for the given bank
                new Document("$match", new Document("bank.$id", new ObjectId(bankId))),
                // Project necessary fields with correct conditional logic and type conversions
                new Document(
                    "$project",
                    new Document(
                        "salesNetPrice",
                        new Document(
                            "$cond",
                            new Document("if", new Document("$eq", Arrays.asList("$kind", "SALE")))
                                .append("then", new Document("$toDouble", "$net_price"))
                                .append("else", 0L)
                        )
                    )
                        .append(
                            "salesNetCost",
                            new Document(
                                "$cond",
                                new Document("if", new Document("$eq", Arrays.asList("$kind", "SALE")))
                                    .append("then", new Document("$toDouble", "$net_cost"))
                                    .append("else", 0L)
                            )
                        )
                        .append(
                            "purchaseNetPrice",
                            new Document(
                                "$cond",
                                new Document("if", new Document("$eq", Arrays.asList("$kind", "PURCHASE")))
                                    .append("then", new Document("$toDouble", "$net_price"))
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
                ),
                // Group stage to calculate totals
                new Document(
                    "$group",
                    new Document("_id", new BsonNull())
                        .append("totalNetSales", new Document("$sum", "$salesNetPrice"))
                        .append("totalNetCostSales", new Document("$sum", "$salesNetCost"))
                        .append("totalNetPurchases", new Document("$sum", "$purchaseNetPrice"))
                        .append("totalNetCostPurchases", new Document("$sum", "$purchaseNetCost"))
                ),
                // Final projection stage to perform calculations
                new Document(
                    "$project",
                    new Document("totalNetSales", 1L)
                        .append("totalNetCostSales", 1L)
                        .append("totalNetPurchases", 1L)
                        .append("totalNetCostPurchases", 1L)
                        .append("totalSalesProfits", new Document("$subtract", Arrays.asList("$totalNetSales", "$totalNetCostSales")))
                        .append(
                            "totalPurchasesCost",
                            new Document("$subtract", Arrays.asList("$totalNetPurchases", "$totalNetCostPurchases"))
                        )
                        .append("totalInflow", "$totalNetSales")
                        .append("totalOutflow", "$totalNetCostPurchases")
                        .append("overallBalance", new Document("$subtract", Arrays.asList("$totalNetSales", "$totalNetCostPurchases")))
                )
            );

            // Execute the aggregation query
            MongoCollection<Document> collection = mongoTemplate.getCollection("invoices");
            AggregateIterable<Document> result = collection.aggregate(pipeline);

            // Retrieve the result and map it to a DTO
            Document aggregationResult = result.first(); // Assuming only one result document

            if (aggregationResult == null) {
                log.warn("No results found for bank ID: {}", bankId);
                return new BankBalanceDTO(); // Return an empty DTO if no result found
            }

            // Map the result to BankBalanceDTO
            BankBalanceDTO bankBalance = new BankBalanceDTO();

            // Extract and set values into the DTO, ensuring safe mapping
            bankBalance.setTotalSales(safeGetDouble(aggregationResult, "totalNetSales"));
            bankBalance.setTotalPurchases(safeGetDouble(aggregationResult, "totalNetPurchases"));
            bankBalance.setMoneyInFromInvoices(safeGetDouble(aggregationResult, "totalNetSales"));
            bankBalance.setMoneyOutFromInvoices(safeGetDouble(aggregationResult, "totalNetCostPurchases"));
            bankBalance.setInvoiceProfits(safeGetDouble(aggregationResult, "totalSalesProfits"));
            bankBalance.setTotalMoneyIn(safeGetDouble(aggregationResult, "totalNetSales"));
            bankBalance.setTotalMoneyOut(safeGetDouble(aggregationResult, "totalNetCostPurchases"));

            // Calculate derived fields
            double totalBalance = bankBalance.getTotalMoneyIn() - bankBalance.getTotalMoneyOut();
            bankBalance.setTotalBalance(totalBalance);

            double totalProfits = bankBalance.getInvoiceProfits();
            bankBalance.setTotalProfits(totalProfits);

            double moneyProfits = totalBalance; // Adjust based on your specific logic
            bankBalance.setMoneyProfits(moneyProfits);

            // Log the result for debugging purposes
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
