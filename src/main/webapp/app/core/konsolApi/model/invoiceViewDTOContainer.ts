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
import { InvoiceViewSimpleDTO } from './invoiceViewSimpleDTO';


/**
 * 
 */
export interface InvoiceViewDTOContainer { 
    result?: Array<InvoiceViewSimpleDTO>;
    total?: number;
    totalCost?: number;
    totalPrice?: number;
    netCost?: number;
    netPrice?: number;
    netResult?: number;
    totalDiscount?: number;
}

