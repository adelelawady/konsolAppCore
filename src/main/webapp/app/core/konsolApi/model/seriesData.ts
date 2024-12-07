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
 * Represents a data series in the chart
 */
export interface SeriesData {
  name?: string;
  type?: string;
  data?: Array<number>;
  style?: { [key: string]: object };
  visible?: boolean;
  yAxisIndex?: number;
}
