package com.konsol.core.repository;

import com.konsol.core.domain.Invoice;
import com.konsol.core.domain.Money;
import com.konsol.core.domain.enumeration.MoneyKind;
import java.time.LocalDateTime;
import java.util.List;
import org.bson.Document;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancialDashboardRepository extends MongoRepository<Invoice, String> {
    // Sales and Revenue Queries
    @Query("{ 'createdDate': { $gte: ?0, $lte: ?1 } }")
    List<Invoice> findInvoicesByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Query("{ 'store.id': ?0, 'createdDate': { $gte: ?1, $lte: ?2 } }")
    List<Invoice> findInvoicesByStoreAndDateRange(String storeId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("{ 'account.id': ?0, 'createdDate': { $gte: ?1, $lte: ?2 } }")
    List<Invoice> findInvoicesByAccountAndDateRange(String accountId, LocalDateTime startDate, LocalDateTime endDate);

    // Cash Flow Queries
    @Query("{ 'bank.id': ?0, 'createdDate': { $gte: ?1, $lte: ?2 } }")
    List<Money> findMoneyTransactionsByBankAndDateRange(String bankId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("{ 'createdDate': { $gte: ?0, $lte: ?1 } }")
    List<Money> findMoneyTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    // Invoice Analysis Queries
    @Query("{ 'deferred': true, 'createdDate': { $gte: ?0, $lte: ?1 } }")
    List<Invoice> findDeferredInvoicesByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Query("{ 'expenses': { $gt: 0 }, 'createdDate': { $gte: ?0, $lte: ?1 } }")
    List<Invoice> findInvoicesWithExpensesByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    // Store Performance Queries
    @Query("{ 'store.id': ?0, 'createdDate': { $gte: ?1, $lte: ?2 } }")
    List<Invoice> findStorePerformanceByDateRange(String storeId, LocalDateTime startDate, LocalDateTime endDate);

    // Account Analysis Queries
    @Query("{ 'account.id': ?0, 'createdDate': { $gte: ?1, $lte: ?2 } }")
    List<Invoice> findAccountPerformanceByDateRange(String accountId, LocalDateTime startDate, LocalDateTime endDate);

    // Bank Analysis Queries
    @Query("{ 'bank.id': ?0, 'createdDate': { $gte: ?1, $lte: ?2 } }")
    List<Invoice> findBankTransactionsByDateRange(String bankId, LocalDateTime startDate, LocalDateTime endDate);

    // Aggregation Queries
    @Query(value = "{ 'createdDate': { $gte: ?0, $lte: ?1 } }", count = true)
    long countInvoicesByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "{ 'deferred': true, 'createdDate': { $gte: ?0, $lte: ?1 } }", count = true)
    long countDeferredInvoicesByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    @Query("{ 'date': { $gte: ?0, $lte: ?1 } }")
    List<Invoice> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("{ 'kind': ?0, 'date': { $gte: ?1, $lte: ?2 } }")
    List<Money> findByKindAndDateBetween(MoneyKind kind, LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        {
            "{ $match: { 'date': { $gte: ?0, $lte: ?1 } } }",
            "{ $unwind: '$items' }",
            "{ $group: { " +
            "_id: '$items.itemId', " +
            "totalQuantity: { $sum: '$items.quantity' }, " +
            "totalRevenue: { $sum: { $multiply: ['$items.price', '$items.quantity'] } }, " +
            "totalCost: { $sum: { $multiply: ['$items.cost', '$items.quantity'] } }, " +
            "averagePrice: { $avg: '$items.price' } " +
            "} }",
            "{ $sort: { totalRevenue: -1 } }",
            "{ $limit: 10 }",
        }
    )
    List<Document> getTopSellingItems(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        {
            "{ $match: { 'date': { $gte: ?0, $lte: ?1 } } }",
            "{ $unwind: '$items' }",
            "{ $group: { " +
            "_id: { " +
            "year: { $year: '$date' }, " +
            "month: { $month: '$date' }, " +
            "itemId: '$items.itemId' " +
            "}, " +
            "quantity: { $sum: '$items.quantity' }, " +
            "revenue: { $sum: { $multiply: ['$items.price', '$items.quantity'] } } " +
            "} }",
            "{ $sort: { '_id.year': 1, '_id.month': 1 } }",
        }
    )
    List<Document> getItemSalesTrend(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        {
            "{ $match: { 'date': { $gte: ?0, $lte: ?1 } } }",
            "{ $unwind: '$items' }",
            "{ $group: { " +
            "_id: '$items.category', " +
            "totalRevenue: { $sum: { $multiply: ['$items.price', '$items.quantity'] } }, " +
            "totalCost: { $sum: { $multiply: ['$items.cost', '$items.quantity'] } }, " +
            "totalQuantity: { $sum: '$items.quantity' } " +
            "} }",
            "{ $project: { " +
            "category: '$_id', " +
            "revenue: '$totalRevenue', " +
            "cost: '$totalCost', " +
            "quantity: '$totalQuantity', " +
            "profitMargin: { " +
            "$multiply: [" +
            "{ $divide: [" +
            "{ $subtract: ['$totalRevenue', '$totalCost'] }, " +
            "$totalRevenue" +
            "] }, " +
            "100" +
            "] " +
            "} " +
            "} }",
            "{ $sort: { revenue: -1 } }",
        }
    )
    List<Document> getCategoryPerformance(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        {
            "{ $match: { 'date': { $gte: ?0, $lte: ?1 } } }",
            "{ $unwind: '$items' }",
            "{ $group: { " +
            "_id: { " +
            "storeId: '$storeId', " +
            "itemId: '$items.itemId' " +
            "}, " +
            "totalQuantity: { $sum: '$items.quantity' }, " +
            "totalRevenue: { $sum: { $multiply: ['$items.price', '$items.quantity'] } } " +
            "} }",
            "{ $sort: { totalRevenue: -1 } }",
            "{ $group: { " +
            "_id: '$_id.storeId', " +
            "topItems: { " +
            "$push: { " +
            "itemId: '$_id.itemId', " +
            "quantity: '$totalQuantity', " +
            "revenue: '$totalRevenue' " +
            "} " +
            "} " +
            "} }",
            "{ $project: { " + "storeId: '$_id', " + "topItems: { $slice: ['$topItems', 5] } " + "} }",
        }
    )
    List<Document> getTopItemsByStore(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        {
            "{ $match: { 'date': { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { " +
            "_id: { " +
            "year: { $year: '$date' }, " +
            "month: { $month: '$date' } " +
            "}, " +
            "totalRevenue: { $sum: '$totalAmount' }, " +
            "totalCost: { $sum: '$totalCost' }, " +
            "invoiceCount: { $sum: 1 } " +
            "} }",
            "{ $sort: { '_id.year': 1, '_id.month': 1 } }",
        }
    )
    List<Document> getMonthlyPerformanceMetrics(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        {
            "{ $match: { 'date': { $gte: ?0, $lte: ?1 } } }",
            "{ $unwind: '$items' }",
            "{ $group: { " +
            "    _id: { itemId: '$items.itemId', itemName: '$items.itemName' }, " +
            "    totalQuantity: { $sum: '$items.quantity' }, " +
            "    totalRevenue: { $sum: { $multiply: ['$items.price', '$items.quantity'] } }, " +
            "    totalCost: { $sum: { $multiply: ['$items.cost', '$items.quantity'] } }, " +
            "    averagePrice: { $avg: '$items.price' }, " +
            "    averageCost: { $avg: '$items.cost' } " +
            "} }",
            "{ $project: { " +
            "    itemId: '$_id.itemId', " +
            "    itemName: '$_id.itemName', " +
            "    totalQuantity: 1, " +
            "    totalRevenue: 1, " +
            "    totalCost: 1, " +
            "    averagePrice: 1, " +
            "    averageCost: 1, " +
            "    profitMargin: { " +
            "        $multiply: [" +
            "            { $divide: [" +
            "                { $subtract: ['$totalRevenue', '$totalCost'] }, " +
            "                '$totalRevenue'" +
            "            ] }, " +
            "            100" +
            "        ] " +
            "    } " +
            "} }",
            "{ $sort: { totalRevenue: -1 } }",
        }
    )
    List<Document> getItemSalesAnalysis(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        {
            "{ $match: { 'date': { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { " +
            "    _id: { $dateToString: { format: '%Y-%m', date: '$date' } }, " +
            "    totalRevenue: { $sum: '$totalAmount' }, " +
            "    totalCost: { $sum: '$totalCost' }, " +
            "    invoiceCount: { $sum: 1 }, " +
            "    averageAmount: { $avg: '$totalAmount' } " +
            "} }",
            "{ $sort: { _id: 1 } }",
        }
    )
    List<Document> getMonthlySalesMetrics(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        {
            "{ $match: { 'date': { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { " +
            "    _id: '$storeId', " +
            "    totalRevenue: { $sum: '$totalAmount' }, " +
            "    invoiceCount: { $sum: 1 }, " +
            "    averageAmount: { $avg: '$totalAmount' }, " +
            "    totalDiscount: { $sum: '$discountAmount' }, " +
            "    totalTax: { $sum: '$taxAmount' } " +
            "} }",
            "{ $sort: { totalRevenue: -1 } }",
        }
    )
    List<Document> findStorePerformanceMetrics(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        {
            "{ $match: { 'date': { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { " +
            "    _id: { " +
            "        storeId: '$storeId', " +
            "        kind: '$kind' " +
            "    }, " +
            "    totalAmount: { $sum: '$totalAmount' }, " +
            "    count: { $sum: 1 } " +
            "} }",
            "{ $group: { " +
            "    _id: '$_id.storeId', " +
            "    paymentMethods: { " +
            "        $push: { " +
            "            kind: '$_id.kind', " +
            "            amount: '$totalAmount', " +
            "            count: '$count' " +
            "        } " +
            "    }, " +
            "    totalRevenue: { $sum: '$totalAmount' } " +
            "} }",
        }
    )
    List<Document> getPaymentMethodDistribution(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        {
            "{ $match: { 'date': { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { " +
            "    _id: { $dayOfWeek: '$date' }, " +
            "    totalRevenue: { $sum: '$totalAmount' }, " +
            "    averageAmount: { $avg: '$totalAmount' }, " +
            "    transactionCount: { $sum: 1 } " +
            "} }",
            "{ $sort: { _id: 1 } }",
        }
    )
    List<Document> getDailyPerformancePatterns(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        {
            "{ $match: { 'date': { $gte: ?0, $lte: ?1 } } }",
            "{ $unwind: '$items' }",
            "{ $group: { " +
            "    _id: { itemId: '$items.itemId', itemName: '$items.itemName' }, " +
            "    quantity: { $sum: '$items.quantity' }, " +
            "    revenue: { $sum: { $multiply: ['$items.quantity', '$items.price'] } }, " +
            "    cost: { $sum: { $multiply: ['$items.quantity', '$items.cost'] } } " +
            "} }",
            "{ $project: { " +
            "    _id: 0, " +
            "    itemId: '$_id.itemId', " +
            "    itemName: '$_id.itemName', " +
            "    quantity: 1, " +
            "    revenue: 1, " +
            "    profit: { $subtract: ['$revenue', '$cost'] }, " +
            "    profitMargin: { $multiply: [{ $divide: [{ $subtract: ['$revenue', '$cost'] }, '$revenue'] }, 100] } " +
            "} }",
            "{ $sort: { revenue: -1 } }",
        }
    )
    List<Document> getItemSalesAnalysis(LocalDateTime startDate, LocalDateTime endDate, String storeId);

    @Aggregation(
        {
            "{ $match: { 'date': { $gte: ?0, $lte: ?1 } } }",
            "{ $unwind: '$items' }",
            "{ $group: { " +
            "    _id: { $dateToString: { format: '%Y-%m-%d', date: '$date' } }, " +
            "    totalSales: { $sum: { $multiply: ['$items.quantity', '$items.price'] } } " +
            "} }",
            "{ $sort: { _id: 1 } }",
        }
    )
    List<Document> getDailyItemSalesTrend(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        {
            "{ $match: { 'date': { $gte: ?0, $lte: ?1 } } }",
            "{ $unwind: '$items' }",
            "{ $group: { " +
            "    _id: '$items.category', " +
            "    totalRevenue: { $sum: { $multiply: ['$items.quantity', '$items.price'] } } " +
            "} }",
            "{ $sort: { totalRevenue: -1 } }",
        }
    )
    List<Document> getItemCategoryRevenue(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        {
            "{ $match: { 'date': { $gte: ?0, $lte: ?1 } } }",
            "{ $unwind: '$items' }",
            "{ $group: { " +
            "    _id: '$items.itemId', " +
            "    itemName: { $first: '$items.itemName' }, " +
            "    totalQuantity: { $sum: '$items.quantity' }, " +
            "    totalRevenue: { $sum: { $multiply: ['$items.quantity', '$items.price'] } }, " +
            "    totalCost: { $sum: { $multiply: ['$items.quantity', '$items.cost'] } } " +
            "} }",
            "{ $project: { " +
            "    _id: 0, " +
            "    itemId: '$_id', " +
            "    itemName: 1, " +
            "    totalQuantity: 1, " +
            "    totalRevenue: 1, " +
            "    profitMargin: { " +
            "        $multiply: [" +
            "            { $divide: [{ $subtract: ['$totalRevenue', '$totalCost'] }, '$totalRevenue'] }, " +
            "            100" +
            "        ] " +
            "    } " +
            "} }",
            "{ $sort: { totalQuantity: -1 } }",
            "{ $limit: ?2 }",
        }
    )
    List<Document> getTopSellingItems(LocalDateTime startDate, LocalDateTime endDate, int limit);

    @Aggregation(
        {
            "{ $match: { 'date': { $gte: ?0, $lte: ?1 } } }",
            "{ $unwind: '$items' }",
            "{ $group: { " +
            "    _id: { category: '$items.category', itemId: '$items.itemId' }, " +
            "    itemName: { $first: '$items.itemName' }, " +
            "    revenue: { $sum: { $multiply: ['$items.quantity', '$items.price'] } }, " +
            "    cost: { $sum: { $multiply: ['$items.quantity', '$items.cost'] } } " +
            "} }",
            "{ $project: { " +
            "    _id: 0, " +
            "    category: '$_id.category', " +
            "    itemId: '$_id.itemId', " +
            "    itemName: 1, " +
            "    profitMargin: { " +
            "        $multiply: [" +
            "            { $divide: [{ $subtract: ['$revenue', '$cost'] }, '$revenue'] }, " +
            "            100" +
            "        ] " +
            "    } " +
            "} }",
            "{ $sort: { profitMargin: -1 } }",
        }
    )
    List<Document> getItemProfitabilityAnalysis(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        {
            "{ $match: { 'date': { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { " +
            "_id: { $dateToString: { format: '%Y-%m-%d', date: '$date' } }, " +
            "totalAmount: { $sum: '$totalAmount' }, " +
            "count: { $sum: 1 } " +
            "} }",
            "{ $sort: { '_id': 1 } }",
        }
    )
    List<Document> getDailySalesTrend(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        {
            "{ $match: { 'date': { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { " +
            "_id: { $dateToString: { format: '%Y-%m', date: '$date' } }, " +
            "totalAmount: { $sum: '$totalAmount' }, " +
            "count: { $sum: 1 } " +
            "} }",
            "{ $sort: { '_id': 1 } }",
        }
    )
    List<Document> getMonthlySalesTrend(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        {
            "{ $match: { 'date': { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { " +
            "_id: { $dateToString: { format: '%Y-%m', date: '$date' } }, " +
            "totalSales: { $sum: '$totalAmount' }, " +
            "totalCosts: { $sum: '$totalCost' } " +
            "} }",
            "{ $project: { " +
            "month: '$_id', " +
            "sales: '$totalSales', " +
            "costs: '$totalCosts', " +
            "margin: { " +
            "$multiply: [" +
            "{ $divide: [" +
            "{ $subtract: ['$totalSales', '$totalCosts'] }, " +
            "$totalSales" +
            "] }, " +
            "100" +
            "] " +
            "} " +
            "} }",
            "{ $sort: { month: 1 } }",
        }
    )
    List<Document> getSalesVsCostsComparison(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        {
            "{ $match: { 'date': { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { " +
            "_id: '$store.id', " +
            "storeName: { $first: '$store.name' }, " +
            "totalSales: { $sum: '$totalAmount' }, " +
            "totalCosts: { $sum: '$totalCost' }, " +
            "invoiceCount: { $sum: 1 } " +
            "} }",
            "{ $project: { " +
            "store: '$storeName', " +
            "sales: '$totalSales', " +
            "costs: '$totalCosts', " +
            "count: '$invoiceCount', " +
            "profitMargin: { " +
            "$multiply: [" +
            "{ $divide: [" +
            "{ $subtract: ['$totalSales', '$totalCosts'] }, " +
            "$totalSales" +
            "] }, " +
            "100" +
            "] " +
            "} " +
            "} }",
            "{ $sort: { sales: -1 } }",
        }
    )
    List<Document> getStorePerformanceMetrics(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        {
            "{ $match: { 'date': { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { " +
            "_id: '$account.id', " +
            "accountName: { $first: '$account.name' }, " +
            "totalVolume: { $sum: '$totalAmount' }, " +
            "transactionCount: { $sum: 1 } " +
            "} }",
            "{ $sort: { totalVolume: -1 } }",
            "{ $limit: 10 }",
        }
    )
    List<Document> getTopAccountsByVolume(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        {
            "{ $match: { 'date': { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { " +
            "_id: '$bank.id', " +
            "bankName: { $first: '$bank.name' }, " +
            "totalInflow: { $sum: { $cond: [{ $eq: ['$kind', 'INCOME'] }, '$amount', 0] } }, " +
            "totalOutflow: { $sum: { $cond: [{ $eq: ['$kind', 'EXPENSE'] }, '$amount', 0] } }, " +
            "transactionCount: { $sum: 1 } " +
            "} }",
            "{ $project: { " +
            "bank: '$bankName', " +
            "inflow: '$totalInflow', " +
            "outflow: '$totalOutflow', " +
            "netFlow: { $subtract: ['$totalInflow', '$totalOutflow'] }, " +
            "count: '$transactionCount' " +
            "} }",
            "{ $sort: { netFlow: -1 } }",
        }
    )
    List<Document> getBankTransactionDistribution(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        {
            "{ $match: { 'date': { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { " +
            "_id: null, " +
            "totalSales: { $sum: '$totalAmount' }, " +
            "totalCosts: { $sum: '$totalCost' }, " +
            "invoiceCount: { $sum: 1 } " +
            "} }",
            "{ $project: { " +
            "grossMargin: { " +
            "$multiply: [" +
            "{ $divide: [" +
            "{ $subtract: ['$totalSales', '$totalCosts'] }, " +
            "$totalSales" +
            "] }, " +
            "100" +
            "] " +
            "}, " +
            "averageTicket: { $divide: ['$totalSales', '$invoiceCount'] } " +
            "} }",
        }
    )
    Document getProfitMarginDistribution(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        {
            "{ $match: { 'date': { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { " +
            "_id: { " +
            "store: '$store.id', " +
            "account: '$account.id' " +
            "}, " +
            "storeName: { $first: '$store.name' }, " +
            "accountName: { $first: '$account.name' }, " +
            "totalAmount: { $sum: '$totalAmount' }, " +
            "transactionCount: { $sum: 1 } " +
            "} }",
            "{ $sort: { totalAmount: -1 } }",
        }
    )
    List<Document> getStoreAccountAnalysis(LocalDateTime startDate, LocalDateTime endDate);

    List<Invoice> findByCreatedDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("{'createdDate': {$gte: ?0, $lte: ?1}, 'status': 'COMPLETED'}")
    List<Invoice> findCompletedInvoicesBetweenDates(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        pipeline = {
            "{ $match: { 'createdDate': { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { _id: { $dateToString: { format: '%Y-%m-%d', date: '$createdDate' } }, totalAmount: { $sum: '$totalPrice' } } }",
            "{ $sort: { '_id': 1 } }",
        }
    )
    List<DailyRevenue> getDailyRevenue(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        pipeline = {
            "{ $match: { 'createdDate': { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { _id: { $dateToString: { format: '%Y-%m', date: '$createdDate' } }, totalAmount: { $sum: '$totalPrice' } } }",
            "{ $sort: { '_id': 1 } }",
        }
    )
    List<MonthlyRevenue> getMonthlyRevenue(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        pipeline = {
            "{ $match: { 'createdDate': { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { _id: '$store', totalSales: { $sum: '$totalPrice' }, totalCost: { $sum: '$totalCost' } } }",
            "{ $project: { store: '$_id', totalSales: 1, totalCost: 1, profitMargin: { $subtract: ['$totalSales', '$totalCost'] } } }",
            "{ $sort: { totalSales: -1 } }",
        }
    )
    List<StorePerformance> storePerformanceMetrics(LocalDateTime startDate, LocalDateTime endDate);

    @Aggregation(
        pipeline = {
            "{ $match: { 'createdDate': { $gte: ?0, $lte: ?1 } } }",
            "{ $group: { _id: '$account', totalTransactions: { $sum: 1 }, totalAmount: { $sum: '$totalPrice' } } }",
            "{ $sort: { totalAmount: -1 } }",
            "{ $limit: 10 }",
        }
    )
    List<AccountVolume> topAccountsByVolume(LocalDateTime startDate, LocalDateTime endDate);

    interface DailyRevenue {
        String getId();
        Double getTotalAmount();
    }

    interface MonthlyRevenue {
        String getId();
        Double getTotalAmount();
    }

    interface StorePerformance {
        String getStore();
        Double getTotalSales();
        Double getTotalCost();
        Double getProfitMargin();
    }

    interface AccountVolume {
        String getId();
        Integer getTotalTransactions();
        Double getTotalAmount();
    }
}
