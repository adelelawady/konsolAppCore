package com.konsol.core.repository;

import com.konsol.core.domain.Invoice;
import com.konsol.core.domain.playstation.PlayStationSession;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the PlayStationSession entity.
 */
@Repository
public interface PlayStationSessionRepository extends MongoRepository<PlayStationSession, String> {
    /**
     * Find active session for a device.
     *
     * @param deviceId the id of the device
     * @return the active session if exists
     */
    Optional<PlayStationSession> findByDeviceIdAndActiveTrue(String deviceId);

    List<PlayStationSession> findAllByDeviceIdAndActiveTrue(String deviceId);
    /**
     * Find all sessions where device category matches any of the provided categories.
     */

    Page<PlayStationSession> findAllByContainerIdAndEndTimeIsNotNullOrderByEndTimeDesc(Pageable page, String containerId);

    List<PlayStationSession> findByEndTimeBetween(Instant startDate, Instant endDate);

    List<PlayStationSession> findByStartTimeBetweenAndActiveIsFalse(Instant startDate, Instant endDate);

    Page<PlayStationSession> findAllByEndTimeIsNotNullOrderByEndTimeDesc(Pageable pageable);

    List<PlayStationSession> findByStartTimeBetween(Instant startTime, Instant endTime);

    @Aggregation(
        pipeline = {
            "{ $match: { " + "startTime: { $gte: ?0, $lte: ?1 } " + "}}",
            "{ $addFields: { " + "invoiceId: '$invoice.$id' " + "}}",
            "{ $lookup: { " + "from: 'invoices', " + "localField: 'invoiceId', " + "foreignField: '_id', " + "as: 'invoiceData' " + "}}",
            "{ $unwind: { " + "path: '$invoiceData', " + "preserveNullAndEmptyArrays: true " + "}}",
            "{ $group: { " +
            "_id: null, " +
            "sessionCount: { $sum: 1 }, " +
            "totalPrice: { $sum: { $ifNull: ['$invoiceData.totalPrice', 0] } }, " +
            "totalCost: { $sum: { $ifNull: ['$invoiceData.totalCost', 0] } }, " +
            "netPrice: { $sum: { $ifNull: ['$invoiceData.netPrice', 0] } }, " +
            "netCost: { $sum: { $ifNull: ['$invoiceData.netCost', 0] } }, " +
            "netUserPrice: { $sum: { $ifNull: ['$invoiceData.userNetPrice', 0] } }, " +
            "totalDiscount: { $sum: { $ifNull: ['$invoiceData.discount', 0] } }, " +
            "totalAdditions: { $sum: { $ifNull: ['$invoiceData.additions', 0] } }, " +
            "totalExpenses: { $sum: { $ifNull: ['$invoiceData.expenses', 0] } } " +
            "}}",
        }
    )
    Document calculateSheftStats(Instant startTime, Instant endTime);

    @Aggregation(
        pipeline = {
            "{ $match: { " + "startTime: { $gte: ?0, $lte: ?1 } " + "}}",
            "{ $group: { " +
            "_id: null, " +
            "count: { $sum: 1 }, " +
            "sessions: { $push: { " +
            "id: '$_id', " +
            "startTime: '$startTime', " +
            "active: '$active', " +
            "invoice: { $ifNull: ['$invoice.$id', null] } " +
            "}} " +
            "}}",
        }
    )
    Document debugSessionStats(Instant startTime, Instant endTime);



    Optional<PlayStationSession> findByVarRefId(String varRefId);
}
