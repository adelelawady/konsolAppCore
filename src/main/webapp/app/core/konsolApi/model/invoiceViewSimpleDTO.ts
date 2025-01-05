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
import { ItemSimpleDTO } from './itemSimpleDTO';
import { AccountUserDTO } from './accountUserDTO';
import { StoreDTO } from './storeDTO';


export interface InvoiceViewSimpleDTO { 
    /**
     * Invoice pk 
     */
    pk?: string;
    id?: string;
    kind?: InvoiceViewSimpleDTO.KindEnum;
    totalCost?: number;
    totalPrice?: number;
    discountPer?: number;
    discount?: number;
    additions?: number;
    additionsType?: string;
    netPrice?: number;
    netCost?: number;
    netResult?: number;
    created_by?: string;
    ItemsCount?: string;
    created_date?: string;
    last_modified_by?: string;
    last_modified_date?: string;
    bank?: BankDTO;
    item?: ItemSimpleDTO;
    account?: AccountUserDTO;
    deferred?: boolean;
    store?: StoreDTO;
}
export namespace InvoiceViewSimpleDTO {
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


