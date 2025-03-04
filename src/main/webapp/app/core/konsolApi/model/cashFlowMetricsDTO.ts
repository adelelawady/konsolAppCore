/**
 * Konsol Core API
 * Konsol Core API documentation
 *
 * The version of the OpenAPI document: 0.0.1
 * Contact: adel50ali50@gmail.com
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


/**
 * Data Transfer Object for cash flow metrics and bank balances
 */
export interface CashFlowMetricsDTO { 
    /**
     * Total incoming cash flow
     */
    totalMoneyIn?: number;
    /**
     * Total outgoing cash flow
     */
    totalMoneyOut?: number;
    /**
     * Map of bank ID to balance
     */
    bankBalances?: { [key: string]: number; };
    /**
     * Current total cash position across all accounts
     */
    currentCashPosition?: number;
}

