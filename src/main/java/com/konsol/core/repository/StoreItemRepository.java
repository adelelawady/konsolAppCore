package com.konsol.core.repository;

import com.konsol.core.domain.StoreItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the StoreItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StoreItemRepository extends MongoRepository<StoreItem, String> {
    Optional<StoreItem> findOneByItemIdAndStoreId(String itemId, String storeId);

    List<StoreItem> findAllByStoreId(String storeId);

    List<StoreItem> findAllByItemId(String storeId);
    /* @Aggregation(pipeline = {
        "{" +
            "  'item.$id': ObjectId('?0')" +
            "}",
        "{" +
    "  _id:'$id'," +
    "  total: {" +
    "   " +
    "    $sum:{$convert: {input: '$qty', to : 'decimal', onError: '',onNull: ''}}" +
    "  }" +
    "  ," +
    "  count: { $sum: 1 }" +
    "}"
    })
    AggregationResults sumItemQtyById(String itemId); */

}
