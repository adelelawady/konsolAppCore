package com.konsol.core.service;

import com.konsol.core.domain.Invoice;
import com.konsol.core.domain.Item;
import com.konsol.core.service.api.dto.*;
import com.konsol.core.service.mapper.InvoiceMapper;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.bson.Document;
import org.bson.types.Decimal128;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * Service Interface for managing all app Queries {@link org.springframework.data.mongodb.core.query.Query}.
 */
@Service
public class MongoQueryService {

    @Autowired
    public final MongoTemplate mongoTemplate;

    @Autowired
    private final InvoiceMapper invoiceMapper;

    public MongoQueryService(MongoTemplate mongoTemplate, InvoiceMapper invoiceMapper) {
        this.mongoTemplate = mongoTemplate;
        this.invoiceMapper = invoiceMapper;
    }

    // getAllItemsQuery

    public InvoiceViewDTOContainer searchInvoicesQUERY(InvoicesSearchModel invoicesSearchModel) {
        org.springframework.data.mongodb.core.query.Query query = new org.springframework.data.mongodb.core.query.Query();
        org.springframework.data.mongodb.core.query.Query countQuery = new Query();
        /**
         * pagination
         */
        if (invoicesSearchModel.getPage() != null && invoicesSearchModel.getSize() != null && invoicesSearchModel.getSize() > 0) {
            Pageable pageable = PageRequest.of(invoicesSearchModel.getPage(), invoicesSearchModel.getSize());
            query.with(pageable);
        }

        /**
         * sort
         */
        if (invoicesSearchModel.getSortField() != null && !invoicesSearchModel.getSortField().isEmpty()) {
            if (invoicesSearchModel.getSortOrder() != null && !invoicesSearchModel.getSortOrder().isEmpty()) {
                switch (invoicesSearchModel.getSortOrder().toLowerCase()) {
                    case "asc":
                        {
                            query.with(Sort.by(Sort.Direction.ASC, invoicesSearchModel.getSortField()));
                            break;
                        }
                    case "desc":
                        {
                            query.with(Sort.by(Sort.Direction.DESC, invoicesSearchModel.getSortField()));
                            break;
                        }
                }
            } else {
                query.with(Sort.by(Sort.Direction.DESC, invoicesSearchModel.getSortField()));
            }
        } else {
            query.with(Sort.by(Sort.Direction.ASC, "pk"));
        }

        /**
         * query plain text
         */
        if (invoicesSearchModel.getSearchText() != null && !invoicesSearchModel.getSearchText().isEmpty()) {
            ObjectId objectId = new ObjectId();
            if (ObjectId.isValid(invoicesSearchModel.getSearchText())) {
                objectId = new ObjectId(invoicesSearchModel.getSearchText());
            } else {
                objectId = null;
            }

            Criteria orCriteria = new Criteria()
                .orOperator(
                    Criteria.where("_id").is(objectId),
                    Criteria.where("pk").regex(invoicesSearchModel.getSearchText(), "i"),
                    Criteria.where("total_cost").regex(invoicesSearchModel.getSearchText(), "i"),
                    Criteria.where("total_price").regex(invoicesSearchModel.getSearchText(), "i"),
                    Criteria.where("discount_per").regex(invoicesSearchModel.getSearchText(), "i"),
                    Criteria.where("discount").regex(invoicesSearchModel.getSearchText(), "i"),
                    Criteria.where("additions").regex(invoicesSearchModel.getSearchText(), "i"),
                    Criteria.where("net_cost").regex(invoicesSearchModel.getSearchText(), "i"),
                    Criteria.where("net_price").regex(invoicesSearchModel.getSearchText(), "i"),
                    Criteria.where("net_result").regex(invoicesSearchModel.getSearchText(), "i"),
                    Criteria.where("expenses").regex(invoicesSearchModel.getSearchText(), "i"),
                    Criteria.where("expenses_type").regex(invoicesSearchModel.getSearchText(), "i"),
                    Criteria.where("created_by").regex(invoicesSearchModel.getSearchText(), "i"),
                    Criteria.where("last_modified_by").regex(invoicesSearchModel.getSearchText(), "i")
                );

            query.addCriteria(orCriteria);
        }

        /**
         * static active & temp
         */
        Criteria andCriteria = new Criteria()
            .andOperator(
                Criteria.where("active").is(true),
                // Criteria.where("temp").is(false),
                (
                    (invoicesSearchModel.getKind() != null && (!invoicesSearchModel.getKind().toString().equals("ALL")))
                        ? Criteria.where("kind").is(invoicesSearchModel.getKind())
                        : Criteria.where("kind").ne(null)
                )
            );

        query.addCriteria(andCriteria);
        /**
         * date
         */
        if ((invoicesSearchModel.getDateFrom() != null)) {
            andCriteria
                .and("created_date")
                .gte(Date.from(invoicesSearchModel.getDateFrom().toInstant()))
                .lt(Date.from(invoicesSearchModel.getDateTo().toInstant()));
        }

        countQuery.addCriteria(andCriteria);

        InvoiceViewDTOContainer invoiceViewDTOContainer = new InvoiceViewDTOContainer();

        List<Invoice> invoicesFound = mongoTemplate.find(query, Invoice.class);
        Long ResultCount = mongoTemplate.count(countQuery, Invoice.class);

        invoiceViewDTOContainer.setResult(invoicesFound.stream().map(invoiceMapper::toInvoiceViewSimpleDTO).collect(Collectors.toList()));

        invoiceViewDTOContainer.setTotal(ResultCount.intValue());

        return calcInvoicesSearchQuery(invoiceViewDTOContainer);
    }

    public InvoiceViewDTOContainer calcInvoicesSearchQuery(InvoiceViewDTOContainer invoiceViewDTOContainer) {
        /*

        {
            _id: "InvoicesReport",
                totalPrice: {
            $sum: {
                $convert: {
                    input: '$total_price',
                        to: 'decimal',
                        onError: 0,
                        onNull: 0
                }
            }
        },
            total_cost: {
                $sum: {
                    $convert: {
                        input: '$total_cost',
                            to: 'decimal',
                            onError: 0,
                            onNull: 0
                    }
                }
            },
            net_cost: {
                $sum: {
                    $convert: {
                        input: '$net_cost',
                            to: 'decimal',
                            onError: 0,
                            onNull: 0
                    }
                }
            },
            net_price: {
                $sum: {
                    $convert: {
                        input: '$net_price',
                            to: 'decimal',
                            onError: 0,
                            onNull: 0
                    }
                }
            },
            net_reslut: {
                $sum: {
                    $convert: {
                        input: '$net_reslut',
                            to: 'decimal',
                            onError: 0,
                            onNull: 0
                    }
                }
            },
            totalDiscount: {
                $sum: {
                    $convert: {
                        input: '$discount',
                            to: 'decimal',
                            onError: 0,
                            onNull: 0
                    }
                }
            },
            invoicesCount:{
                $sum:1
            }
        }
 */

        MongoCollection<Document> collection = mongoTemplate.getCollection("invoices");

        AggregateIterable<Document> result = collection.aggregate(
            Arrays.asList(
                new Document(
                    "$match",
                    new Document(
                        "_id",
                        new Document(
                            "$in",
                            invoiceViewDTOContainer
                                .getResult()
                                .stream()
                                .map(InvoiceViewSimpleDTO::getId)
                                .map(ObjectId::new)
                                .collect(Collectors.toList())
                        )
                    )
                ),
                new Document(
                    "$group",
                    new Document("_id", "InvoicesReport")
                        .append(
                            "totalPrice",
                            new Document(
                                "$sum",
                                new Document(
                                    "$convert",
                                    new Document("input", "$total_price").append("to", "decimal").append("onError", 0L).append("onNull", 0L)
                                )
                            )
                        )
                        .append(
                            "totalCost",
                            new Document(
                                "$sum",
                                new Document(
                                    "$convert",
                                    new Document("input", "$total_cost").append("to", "decimal").append("onError", 0L).append("onNull", 0L)
                                )
                            )
                        )
                        .append(
                            "netCost",
                            new Document(
                                "$sum",
                                new Document(
                                    "$convert",
                                    new Document("input", "$net_cost").append("to", "decimal").append("onError", 0L).append("onNull", 0L)
                                )
                            )
                        )
                        .append(
                            "netPrice",
                            new Document(
                                "$sum",
                                new Document(
                                    "$convert",
                                    new Document("input", "$net_price").append("to", "decimal").append("onError", 0L).append("onNull", 0L)
                                )
                            )
                        )
                        .append(
                            "netResult",
                            new Document(
                                "$sum",
                                new Document(
                                    "$convert",
                                    new Document("input", "$net_result").append("to", "decimal").append("onError", 0L).append("onNull", 0L)
                                )
                            )
                        )
                        .append(
                            "totalDiscount",
                            new Document(
                                "$sum",
                                new Document(
                                    "$convert",
                                    new Document("input", "$discount").append("to", "decimal").append("onError", 0L).append("onNull", 0L)
                                )
                            )
                        )
                        .append("invoicesCount", new Document("$sum", 1L))
                )
            )
        );

        MongoCursor<Document> iterator = result.iterator();

        if (iterator.hasNext()) {
            var tResult = iterator.next();
            Decimal128 totalPrice = Decimal128.parse(tResult.get("totalPrice").toString());
            Decimal128 totalCost = Decimal128.parse(tResult.get("totalCost").toString());
            Decimal128 totalNetPrice = Decimal128.parse(tResult.get("netPrice").toString());
            Decimal128 totalNetCost = Decimal128.parse(tResult.get("netCost").toString());
            Decimal128 netResult = Decimal128.parse(tResult.get("netResult").toString());
            Decimal128 totalDiscount = Decimal128.parse(tResult.get("totalDiscount").toString());
            Decimal128 invoicesCount = Decimal128.parse(tResult.get("invoicesCount").toString());

            invoiceViewDTOContainer.setTotalPrice(totalPrice.bigDecimalValue());
            invoiceViewDTOContainer.setTotalCost(totalCost.bigDecimalValue());
            invoiceViewDTOContainer.setNetPrice(totalNetPrice.bigDecimalValue());
            invoiceViewDTOContainer.setNetCost(totalNetCost.bigDecimalValue());
            invoiceViewDTOContainer.setNetResult(netResult.bigDecimalValue());
            invoiceViewDTOContainer.setTotalDiscount(totalDiscount.bigDecimalValue());
        }
        return invoiceViewDTOContainer;
    }

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
