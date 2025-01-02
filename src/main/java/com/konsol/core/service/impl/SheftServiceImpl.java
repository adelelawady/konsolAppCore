package com.konsol.core.service.impl;

import com.konsol.core.domain.Invoice;
import com.konsol.core.domain.InvoiceItem;
import com.konsol.core.domain.Sheft;
import com.konsol.core.domain.User;
import com.konsol.core.domain.enumeration.SheftStatus;
import com.konsol.core.domain.playstation.PlayStationSession;
import com.konsol.core.repository.PlayStationSessionRepository;
import com.konsol.core.repository.SheftRepository;
import com.konsol.core.security.SecurityUtils;
import com.konsol.core.service.SheftService;
import com.konsol.core.service.UserService;
import com.konsol.core.service.api.dto.PsSessionDTO;
import com.konsol.core.service.api.dto.SheftDTO;
import com.konsol.core.service.mapper.PlayStationSessionMapper;
import com.konsol.core.service.mapper.SheftMapper;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import java.math.BigDecimal;
import java.time.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bson.BsonNull;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing {@link com.konsol.core.domain.Sheft}.
 */
@Service
public class SheftServiceImpl implements SheftService {

    private static final Logger LOG = LoggerFactory.getLogger(SheftServiceImpl.class);

    private final SheftRepository sheftRepository;

    private final SheftMapper sheftMapper;
    private final UserService userService;

    private final PlayStationSessionMapper playStationSessionMapper;
    private final PlayStationSessionRepository playStationSessionRepository;
    private final MongoTemplate mongoTemplate;

    public SheftServiceImpl(
        SheftRepository sheftRepository,
        SheftMapper sheftMapper,
        UserService userService,
        PlayStationSessionMapper playStationSessionMapper,
        PlayStationSessionRepository playStationSessionRepository,
        MongoTemplate mongoTemplate
    ) {
        this.sheftRepository = sheftRepository;
        this.sheftMapper = sheftMapper;
        this.userService = userService;
        this.playStationSessionMapper = playStationSessionMapper;
        this.playStationSessionRepository = playStationSessionRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public SheftDTO save(SheftDTO sheftDTO) {
        LOG.debug("Request to save Sheft : {}", sheftDTO);
        Sheft sheft = sheftMapper.toEntity(sheftDTO);
        sheft = sheftRepository.save(sheft);
        return sheftMapper.toDto(sheft);
    }

    @Override
    public SheftDTO update(SheftDTO sheftDTO) {
        LOG.debug("Request to update Sheft : {}", sheftDTO);
        Sheft sheft = sheftMapper.toEntity(sheftDTO);
        sheft = sheftRepository.save(sheft);
        return sheftMapper.toDto(sheft);
    }

    @Override
    public Optional<SheftDTO> partialUpdate(SheftDTO sheftDTO) {
        LOG.debug("Request to partially update Sheft : {}", sheftDTO);

        return sheftRepository
            .findById(sheftDTO.getId())
            .map(existingSheft -> {
                sheftMapper.partialUpdate(existingSheft, sheftDTO);

                return existingSheft;
            })
            .map(sheftRepository::save)
            .map(sheftMapper::toDto);
    }

    @Override
    public Page<SheftDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Shefts");
        return sheftRepository.findAll(pageable).map(sheftMapper::toDto);
    }

    @Override
    public Optional<SheftDTO> findOne(String id) {
        LOG.debug("Request to get Sheft : {}", id);

        Optional<Sheft> sheftOptional = sheftRepository.findById(id);

        if (sheftOptional.isEmpty()) {
            return Optional.empty();
        }
        if (sheftOptional.get().getActive()) {
            calculateSheft(id);
            return sheftRepository.findById(id).map(sheftMapper::toDto);
        }
        return sheftOptional.map(sheftMapper::toDto);
    }

    @Override
    public void delete(String id) {
        LOG.debug("Request to delete Sheft : {}", id);
        sheftRepository.deleteById(id);
    }

    @Override
    public Sheft startSheft() {
        LOG.debug("Request to start new Sheft");

        // Check if there's already an active shift
        Optional<Sheft> activeSheft = sheftRepository.findByActiveTrue();
        if (activeSheft.isPresent()) {
            return activeSheft.get();
            //throw new IllegalStateException("There is already an active shift. Please end the current shift before starting a new one.");
        }

        // Create new Sheft instance
        Sheft sheft = new Sheft();

        // Set start time to current timestamp
        sheft.setStartTime(Instant.now());
        sheft.setEndTime(null);
        sheft.setDuration(Duration.ZERO);
        // Set active status
        sheft.setActive(true);

        // Get current authenticated user
        Optional<User> currentUser = Optional.ofNullable(
            userService.getCurrentUserLogin().orElseThrow(() -> new IllegalStateException("Current user not found"))
        );
        if (currentUser.isEmpty()) {
            throw new IllegalStateException("Current user not found");
        }
        sheft.setAssignedEmployee(currentUser.get().getFirstName() + " " + currentUser.get().getLastName());
        sheft.setAssignedEmployeeUser(currentUser.get());

        // Initialize required fields with default values
        sheft.setTotalprice(BigDecimal.ZERO);
        sheft.setTotalCost(BigDecimal.ZERO);
        sheft.setNetPrice(BigDecimal.ZERO);
        sheft.setNetCost(BigDecimal.ZERO);
        sheft.setNetUserPrice(BigDecimal.ZERO);
        sheft.setTotalItemsOut(BigDecimal.ZERO);
        sheft.setDiscount(BigDecimal.ZERO);
        sheft.setInvoicesAdditions(BigDecimal.ZERO);
        sheft.setAdditions(BigDecimal.ZERO);
        sheft.setAdditionsNotes(BigDecimal.ZERO);
        sheft.setInvoicesExpenses(BigDecimal.ZERO);
        sheft.setSheftExpenses(BigDecimal.ZERO);
        sheft.setTotalinvoices(BigDecimal.ZERO);
        sheft.setTotaldeletedItems(BigDecimal.ZERO);
        sheft.setTotaldeletedItemsPrice(BigDecimal.ZERO);

        // Save the new sheft
        return sheftRepository.save(sheft);
    }

    @Override
    public SheftDTO getCurrentSheft() {
        return this.sheftMapper.toDto(startSheft());
    }

    @Override
    public Sheft endSheft() {
        LOG.debug("Request to end current Sheft");

        // Check if there's already an active shift
        Optional<Sheft> activeSheft = sheftRepository.findByActiveTrue();
        if (activeSheft.isEmpty()) {
            throw new IllegalStateException("There is no active shift to end.");
        }

        Sheft sheft = activeSheft.get();

        // Set end time to current timestamp
        sheft.setEndTime(Instant.now());
        sheft.setDuration(Duration.between(sheft.getStartTime(), sheft.getEndTime()));

        // Set active status
        sheft.setActive(false);

        // Save the updated sheft
        sheftRepository.save(sheft);

        // Calculate the sheft stats
        calculateSheft(sheft.getId());

        return sheftRepository
            .findById(sheft.getId())
            .orElseThrow(() -> new IllegalStateException("Sheft not found with id: " + sheft.getId()));
    }

    @Override
    public void calculateSheft(String id) {
        LOG.debug("Request to calculate Sheft : {}", id);

        Sheft sheft = sheftRepository.findById(id).orElseThrow(() -> new IllegalStateException("Sheft not found with id: " + id));

        LOG.debug("Calculating stats from {} to {}", sheft.getStartTime(), Instant.now());

        MongoCollection<Document> collection = mongoTemplate.getCollection("ps_session");
        AggregateIterable<Document> result = collection.aggregate(
            Arrays.asList(
                new Document("$match", new Document("end_time", new Document("$gte", sheft.getStartTime()).append("$lte", Instant.now()))),
                new Document("$addFields", new Document("invoiceId", "$invoice.$id")),
                new Document(
                    "$lookup",
                    new Document("from", "invoices")
                        .append("localField", "invoiceId")
                        .append("foreignField", "_id")
                        .append("as", "invoiceData")
                ),
                new Document("$unwind", new Document("path", "$invoiceData").append("preserveNullAndEmptyArrays", true)),
                new Document(
                    "$group",
                    new Document("_id", new BsonNull())
                        .append("sessionCount", new Document("$sum", 1L))
                        .append(
                            "totalPrice",
                            new Document(
                                "$sum",
                                new Document("$ifNull", Arrays.asList(new Document("$toDecimal", "$invoiceData.total_price"), 0L))
                            )
                        )
                        .append(
                            "totalCost",
                            new Document(
                                "$sum",
                                new Document("$ifNull", Arrays.asList(new Document("$toDecimal", "$invoiceData.total_cost"), 0L))
                            )
                        )
                        .append(
                            "netPrice",
                            new Document(
                                "$sum",
                                new Document("$ifNull", Arrays.asList(new Document("$toDecimal", "$invoiceData.net_price"), 0L))
                            )
                        )
                        .append(
                            "netCost",
                            new Document(
                                "$sum",
                                new Document("$ifNull", Arrays.asList(new Document("$toDecimal", "$invoiceData.net_cost"), 0L))
                            )
                        )
                        .append(
                            "netUserPrice",
                            new Document(
                                "$sum",
                                new Document("$ifNull", Arrays.asList(new Document("$toDecimal", "$invoiceData.user_net_price"), 0L))
                            )
                        )
                        .append(
                            "totalDiscount",
                            new Document(
                                "$sum",
                                new Document("$ifNull", Arrays.asList(new Document("$toDecimal", "$invoiceData.discount"), 0L))
                            )
                        )
                        .append(
                            "totalAdditions",
                            new Document(
                                "$sum",
                                new Document("$ifNull", Arrays.asList(new Document("$toDecimal", "$invoiceData.additions"), 0L))
                            )
                        )
                        .append(
                            "totalExpenses",
                            new Document(
                                "$sum",
                                new Document("$ifNull", Arrays.asList(new Document("$toDecimal", "$invoiceData.expenses"), 0L))
                            )
                        )
                )
            )
        );

        if (result != null) {
            Document firstDoc = result.first();
            if (firstDoc != null) {
                LOG.debug("Main aggregation result: {}", firstDoc.toJson());

                // Set sessions count
                sheft.setTotalinvoices(BigDecimal.valueOf(firstDoc.getLong("sessionCount")));

                // Set financial totals
                sheft.setTotalprice(getBigDecimalFromDocument(result, "totalPrice"));
                sheft.setTotalCost(getBigDecimalFromDocument(result, "totalCost"));
                sheft.setNetPrice(getBigDecimalFromDocument(result, "netPrice"));
                sheft.setNetCost(getBigDecimalFromDocument(result, "netCost"));
                sheft.setNetUserPrice(getBigDecimalFromDocument(result, "netUserPrice"));
                sheft.setDiscount(getBigDecimalFromDocument(result, "totalDiscount"));
                sheft.setInvoicesAdditions(getBigDecimalFromDocument(result, "totalAdditions"));
                sheft.setInvoicesExpenses(getBigDecimalFromDocument(result, "totalExpenses"));

                LOG.debug("Updated sheft with calculated values: {}", sheft);
            } else {
                LOG.warn("No results found in aggregation for sheft {}", id);
            }
        }

        // Calculate duration if end time exists
        if (sheft.getEndTime() != null) {
            Duration duration = Duration.between(sheft.getStartTime(), Instant.now());
            sheft.setDuration(duration);
        }
        sheft.getSessions().clear();
        sheft.getSessions().addAll(playStationSessionRepository.findByEndTimeBetween(sheft.getStartTime(), Instant.now()));

        // Calculate total items out from all sessions
        BigDecimal totalItemsOut = sheft
            .getSessions()
            .stream()
            .filter(session -> session.getInvoice() != null)
            .map(session ->
                session
                    .getInvoice()
                    .getInvoiceItems()
                    .stream()
                    .filter(item -> !item.isBuildIn())
                    .map(InvoiceItem::getQtyOut)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
            )
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        sheft.setTotalItemsOut(totalItemsOut);

        // Save updated sheft
        sheftRepository.save(sheft);
    }

    @Override
    public void reCalculateActiveSheftIfAvailable() {
        sheftRepository.findByActiveTrue().ifPresent(sheft -> calculateSheft(sheft.getId()));
    }

    @Override
    public List<PsSessionDTO> activeSheftSessions() {
        Optional<Sheft> optionalSheft = sheftRepository.findByActiveTrue();
        return optionalSheft
            .map(sheft ->
                playStationSessionRepository
                    .findByEndTimeBetween(sheft.getStartTime(), Instant.now())
                    .stream()
                    .map(playStationSessionMapper::toDto)
                    .collect(Collectors.toList())
            )
            .orElse(Collections.emptyList());
    }

    // Helper method to extract ID from various possible formats
    private String extractId(Object idObj) {
        if (idObj == null) return null;

        if (idObj instanceof Document) {
            // Handle MongoDB ObjectId document
            return ((Document) idObj).getString("$oid");
        }

        // Handle direct string or other types
        return String.valueOf(idObj);
    }

    private BigDecimal getBigDecimalFromDocument(AggregateIterable<Document> result, String field) {
        Document doc = result.first();
        if (doc == null) return BigDecimal.ZERO;

        Object value = doc.get(field);
        if (value == null) return BigDecimal.ZERO;

        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        } else if (value instanceof Document) {
            // Handle case where value might be a Decimal128
            Document decimalDoc = (Document) value;
            if (decimalDoc.containsKey("$numberDecimal")) {
                String decimalStr = decimalDoc.getString("$numberDecimal");
                try {
                    return new BigDecimal(decimalStr);
                } catch (NumberFormatException e) {
                    LOG.warn("Failed to parse decimal value: {}", decimalStr);
                    return BigDecimal.ZERO;
                }
            }
        }

        LOG.warn("Unexpected value type for field {}: {}", field, value.getClass());
        return BigDecimal.ZERO;
    }
}
