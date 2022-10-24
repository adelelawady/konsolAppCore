package com.konsol.core.service;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import java.util.Arrays;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;

/**
 * Service Interface for managing all app Queries {@link org.springframework.data.mongodb.core.query.Query}.
 */
@Service
public class MongoQueryService {

    @Autowired
    public final MongoTemplate mongoTemplate;

    public MongoQueryService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    // getAllItemsQuery

    /**
     * calculate total price , total cost for all invoice items and
     * sum them
     * @param invoiceId invoice to calculate
     * @return totalPrice , totalCost , totalNetPrice , totalNetCost
     */
    public AggregateIterable<Document> calculateInvoiceQUERY(String invoiceId) {
        //docs
        /*
        [{
    $match: {
        _id: ObjectId('63434c2d11fa063c76d7355f')
    }
}, {
    $lookup: {
        from: 'invoice_items',
        localField: 'invoiceItems.$id',
        foreignField: '_id',
        as: 'result'
    }
}, {
    $unwind: {
        path: '$result',
        includeArrayIndex: 'string',
        preserveNullAndEmptyArrays: false
    }
}, {
    $group: {
        _id: '$_id',
        totalPrice: {
            $sum: {
                $convert: {
                    input: '$result.total_price',
                    to: 'decimal',
                    onError: 0,
                    onNull: 0
                }
            }
        },
        totalCost: {
            $sum: {
                $convert: {
                    input: '$result.total_cost',
                    to: 'decimal',
                    onError: 0,
                    onNull: 0
                }
            }
        },
        totalNetPrice: {
            $sum: {
                $convert: {
                    input: '$result.net_price',
                    to: 'decimal',
                    onError: 0,
                    onNull: 0
                }
            }
        },
        totalNetCost: {
            $sum: {
                $convert: {
                    input: '$result.net_cost',
                    to: 'decimal',
                    onError: 0,
                    onNull: 0
                }
            }
        }
    }
}]
         */

        MongoCollection<Document> collection = mongoTemplate.getCollection("invoices");

        return collection.aggregate(
            Arrays.asList(
                new Document("$match", new Document("_id", new ObjectId(invoiceId))),
                new Document(
                    "$lookup",
                    new Document("from", "invoice_items")
                        .append("localField", "invoiceItems.$id")
                        .append("foreignField", "_id")
                        .append("as", "result")
                ),
                new Document(
                    "$unwind",
                    new Document("path", "$result").append("includeArrayIndex", "string").append("preserveNullAndEmptyArrays", false)
                ),
                new Document(
                    "$group",
                    new Document("_id", "$_id")
                        .append(
                            "totalPrice",
                            new Document(
                                "$sum",
                                new Document(
                                    "$convert",
                                    new Document("input", "$result.total_price")
                                        .append("to", "decimal")
                                        .append("onError", 0L)
                                        .append("onNull", 0L)
                                )
                            )
                        )
                        .append(
                            "totalCost",
                            new Document(
                                "$sum",
                                new Document(
                                    "$convert",
                                    new Document("input", "$result.total_cost")
                                        .append("to", "decimal")
                                        .append("onError", 0L)
                                        .append("onNull", 0L)
                                )
                            )
                        )
                        .append(
                            "totalNetPrice",
                            new Document(
                                "$sum",
                                new Document(
                                    "$convert",
                                    new Document("input", "$result.net_price")
                                        .append("to", "decimal")
                                        .append("onError", 0L)
                                        .append("onNull", 0L)
                                )
                            )
                        )
                        .append(
                            "totalNetCost",
                            new Document(
                                "$sum",
                                new Document(
                                    "$convert",
                                    new Document("input", "$result.net_cost")
                                        .append("to", "decimal")
                                        .append("onError", 0L)
                                        .append("onNull", 0L)
                                )
                            )
                        )
                )
            )
        );
    }
}
