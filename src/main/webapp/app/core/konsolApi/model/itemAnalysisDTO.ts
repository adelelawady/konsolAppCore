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

export interface ItemAnalysisDTO {
  /**
   * Total gross sales (إجمالي المبيعات الإجمالية).
   */
  totalSales?: number;
  /**
   * Total net sales (إجمالي المبيعات الصافية).
   */
  netSales?: number;
  /**
   * Total gross cost (إجمالي التكلفة الإجمالية).
   */
  totalCost?: number;
  /**
   * Total net cost (إجمالي التكلفة الصافية).
   */
  netCost?: number;
  /**
   * Total profits (إجمالي الأرباح).
   */
  totalProfit?: number;
  /**
   * Total discounts (إجمالي الخصومات).
   */
  totalDiscount?: number;
  /**
   * Total quantity sold (إجمالي الكمية المباعة).
   */
  totalQtyOut?: number;
  /**
   * Total quantity purchased (إجمالي الكمية المشتراة).
   */
  totalQtyIn?: number;
}
