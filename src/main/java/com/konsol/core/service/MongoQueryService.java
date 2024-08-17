package com.konsol.core.service;

import com.konsol.core.domain.Invoice;
import com.konsol.core.repository.AccountUserRepository;
import com.konsol.core.repository.MoneyRepository;
import com.konsol.core.service.api.dto.*;
import com.konsol.core.service.mapper.AccountUserMapper;
import com.konsol.core.service.mapper.InvoiceMapper;
import com.konsol.core.service.mapper.MoneyMapper;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Aggregates;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.bson.BsonDocument;
import org.bson.BsonValue;
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

    @Autowired
    private MoneyMapper moneyMapper;

    @Autowired
    private MoneyRepository moneyRepository;

    @Autowired
    private AccountUserRepository accountUserRepository;

    @Autowired
    private AccountUserMapper accountUserMapper;

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
                    //Criteria.where("_id").is(objectId),
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
     *
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

    /* UPDATED */
    /*
   import java.util.Arrays;
import org.bson.Document;
import org.bson.types.ObjectId;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.conversions.Bson;
import java.util.concurrent.TimeUnit;
import org.bson.Document;


      Requires the MongoDB Java Driver.
      https://mongodb.github.io/mongo-java-driver


    MongoClient mongoClient = new MongoClient(
        new MongoClientURI(
            "mongodb://localhost:27017/"
        )
    );
    MongoDatabase database = mongoClient.getDatabase("KonsolCore");
    MongoCollection<Document> collection = database.getCollection("monies");

    FindIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$match",
        new Document("$or", Arrays.asList(new Document("$and", Arrays.asList(new Document("kind", "PAYMENT"),
                new Document("created_date",
                    new Document("$gte", "2022-12-20T10:47:50.498+00:00")),
                new Document("created_date",
                    new Document("$lt", "2022-12-29T10:47:50.498+00:00")))),
            new Document("pk", "2"),
            new Document("_id",
                new ObjectId("641331ab3a789e767dec3d2c")),
            new Document("bank.$id",
                new ObjectId("6412e314ec9e5356ae48c5f2")),
            new Document("account.$id",
                new ObjectId("6412e714ec9e5356ae48c5f2")),
            new Document("item.$id",
                new ObjectId("6412e714e49e5356ae48c5f2")))))));

     */

    public MoniesViewDTOContainer moniesViewSearchPaginate(MoniesSearchModel moniesSearchModel) {
        MongoCollection<Document> collection = mongoTemplate.getCollection("monies");
        //.withCodecRegistry(pojoCodecRegistry);

        List<Document> staticOrList = new ArrayList<>();

        if (moniesSearchModel.getBankId() != null) {
            staticOrList.add(new Document("bank.$id", new ObjectId(moniesSearchModel.getBankId())));
        }
        if (moniesSearchModel.getAccountId() != null) {
            staticOrList.add(new Document("account._id", new ObjectId(moniesSearchModel.getAccountId())));
        }
        if (moniesSearchModel.getPk() != null) {
            staticOrList.add(new Document("pk", moniesSearchModel.getPk()));
        }
        if (moniesSearchModel.getId() != null) {
            staticOrList.add(new Document("_id", new ObjectId(moniesSearchModel.getId())));
        }

        // Details
        if (moniesSearchModel.getDetails() != null) {
            staticOrList.add(
                new Document("details", new Document("$regex", moniesSearchModel.getDetails().toString()).append("$options", "i"))
            );
        }

        List<Document> mainANDList = new ArrayList<>();

        if (!staticOrList.isEmpty()) {
            mainANDList = new ArrayList<>(List.of(new Document("$or", staticOrList)));
        } else {
            mainANDList = new ArrayList<>();
        }

        // KIND
        if (moniesSearchModel.getKind() != null) {
            mainANDList.add(new Document("kind", moniesSearchModel.getKind().toString()));
        }

        // DATE
        if (moniesSearchModel.getDateFrom() != null && moniesSearchModel.getDateTo() != null) {
            mainANDList.add(
                new Document(
                    "created_date",
                    new Document("$gte", moniesSearchModel.getDateFrom()).append("$lt", moniesSearchModel.getDateTo())
                )
            );
        }

        //TODO important info here for pagination using mongo java service api

        List<Document> resultList;

        if (!mainANDList.isEmpty()) {
            resultList =
                Arrays.asList(
                    new Document("$match", new Document("$and", mainANDList)),
                    new Document("$skip", (moniesSearchModel.getPage()) * moniesSearchModel.getSize()),
                    new Document("$limit", moniesSearchModel.getSize())
                );
        } else {
            resultList =
                Arrays.asList(
                    new Document("$skip", (moniesSearchModel.getPage()) * moniesSearchModel.getSize()),
                    new Document("$limit", moniesSearchModel.getSize())
                );
        }

        AggregateIterable<Document> result = collection.aggregate(resultList);
        MoniesViewDTOContainer moniesViewDTOContainer = new MoniesViewDTOContainer();

        MongoCursor<Document> iterator = result.iterator();
        List<String> ids = new ArrayList<>();
        while (iterator.hasNext()) {
            Document next = iterator.next();
            String id = next.getObjectId("_id").toString();
            ids.add(id);
        }
        Sort sort;
        if (moniesSearchModel.getSortOrder().equals(MoniesSearchModel.SortOrderEnum.DESC)) {
            sort = Sort.by(Sort.Direction.DESC, moniesSearchModel.getSortField());
        } else {
            sort = Sort.by(Sort.Direction.ASC, moniesSearchModel.getSortField());
        }

        moniesViewDTOContainer.total((int) moneyRepository.count());
        moniesViewDTOContainer.result(
            moneyRepository.findAllByIdIn(ids, sort).stream().map(moneyMapper::toDto).collect(Collectors.toList())
        );

        return moniesViewDTOContainer;
    }

    /*

    {
  $or: [
    {
      kind: {
        $regex: ".*value.*",
        $options: "i"
      }
    },
    {
      name: {
        $regex: ".*lica.*",
        $options: "i"
      }
    },
    {
      phone: {
        $regex: ".*value.*",
        $options: "i"
      }
    },
    {
      address: {
        $regex: ".*value.*",
        $options: "i"
      }
    },
    {
      address_2: {
        $regex: ".*value.*",
        $options: "i"
      }
    }
  ]
}

     */

    /**
     * @param accountUserSearchModel
     * @return
     */
    public AccountUserContainer accountUserSearchPaginate(AccountUserSearchModel accountUserSearchModel) {
        List<Document> arrays = new ArrayList<>();

        if (accountUserSearchModel.getKind() != null) {
            arrays.add(new Document("kind", new Document("$regex", accountUserSearchModel.getKind().toString()).append("$options", "i")));
        }

        if (accountUserSearchModel.getName() != null) {
            arrays.add(new Document("name", new Document("$regex", accountUserSearchModel.getName()).append("$options", "i")));
        }

        if (accountUserSearchModel.getPhone() != null) {
            arrays.add(new Document("phone", new Document("$regex", accountUserSearchModel.getPhone()).append("$options", "i")));
        }

        if (accountUserSearchModel.getAddress() != null) {
            arrays.add(new Document("address", new Document("$regex", accountUserSearchModel.getAddress()).append("$options", "i")));
        }

        if (accountUserSearchModel.getAddress2() != null) {
            arrays.add(new Document("address_2", new Document("$regex", accountUserSearchModel.getAddress2()).append("$options", "i")));
        }

        MongoCollection<Document> collection = mongoTemplate.getCollection("accounts");
        AccountUserContainer accountUserContainer = new AccountUserContainer();
        if (!arrays.isEmpty()) {
            //
            AggregateIterable<Document> result;
            if (accountUserSearchModel.getPage() != null) {
                result =
                    collection.aggregate(
                        List.of(
                            new Document("$match", new Document("$or", arrays)),
                            new Document("$skip", (accountUserSearchModel.getPage()) * accountUserSearchModel.getSize()),
                            new Document("$limit", accountUserSearchModel.getSize())
                        )
                    );
            } else {
                result = collection.aggregate(List.of(new Document("$match", new Document("$or", arrays))));
            }

            if (result.iterator().hasNext()) {
                MongoCursor<Document> iterator = result.iterator();
                List<String> ids = new ArrayList<>();
                while (iterator.hasNext()) {
                    Document next = iterator.next();
                    String id = next.getObjectId("_id").toString();
                    ids.add(id);
                }
                accountUserContainer.result(
                    accountUserRepository.findAllByIdIn(ids).stream().map(accountUserMapper::toDto).collect(Collectors.toList())
                );
                accountUserContainer.total((int) accountUserRepository.count());
            }
        } else {
            accountUserContainer.total((int) accountUserRepository.count());

            if (accountUserSearchModel.getPage() != null) {
                Pageable pageable = PageRequest.of(accountUserSearchModel.getPage(), accountUserSearchModel.getSize());
                accountUserContainer.result(
                    accountUserRepository.findAll(pageable).stream().map(accountUserMapper::toDto).collect(Collectors.toList())
                );
            } else {
                accountUserContainer.result(
                    accountUserRepository.findAll().stream().map(accountUserMapper::toDto).collect(Collectors.toList())
                );
            }
        }
        return accountUserContainer;
    }
}
