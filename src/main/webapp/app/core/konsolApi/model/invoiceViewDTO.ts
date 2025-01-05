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
import { BankDTO } from './bankDTO';
import { InvoiceItemViewDTO } from './invoiceItemViewDTO';
import { ItemDTO } from './itemDTO';
import { AccountUserDTO } from './accountUserDTO';
import { StoreDTO } from './storeDTO';


export interface InvoiceViewDTO { 
    /**
     * Invoice pk 
     */
    pk?: string;
    id?: string;
    kind?: InvoiceViewDTO.KindEnum;
    totalCost?: number;
    totalPrice?: number;
    discountPer?: number;
    discount?: number;
    additions?: number;
    additionsType?: string;
    netCost?: number;
    netPrice?: number;
    netResult?: number;
    expenses?: number;
    expensesType?: string;
    bank?: BankDTO;
    item?: ItemDTO;
    account?: AccountUserDTO;
    invoiceItems?: Array<InvoiceItemViewDTO>;
    created_by?: string;
    created_date?: string;
    last_modified_by?: string;
    last_modified_date?: string;
    invoiceItemsCount?: string;
    deferred?: boolean;
    store?: StoreDTO;
}
export namespace InvoiceViewDTO {
    export type KindEnum = 'SALE' | 'PURCHASE' | 'ADJUST' | 'TRANSFER' | 'SALEQUOTE' | 'RETURNPUR' | 'RETURNSALE' | 'COMPONENT';
    export const KindEnum = {
        Sale: 'SALE' as KindEnum,
        Purchase: 'PURCHASE' as KindEnum,
        Adjust: 'ADJUST' as KindEnum,
        Transfer: 'TRANSFER' as KindEnum,
        Salequote: 'SALEQUOTE' as KindEnum,
        Returnpur: 'RETURNPUR' as KindEnum,
        Returnsale: 'RETURNSALE' as KindEnum,
        Component: 'COMPONENT' as KindEnum
    };
}


