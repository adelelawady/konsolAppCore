/**
 * Konsol Core API
 * Konsol Core API documentation
 *
 * OpenAPI spec version: 0.0.1
 * Contact: adel50ali50@gmail.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */
import { AccountUserDTO } from './accountUserDTO';
import { BankDTO } from './bankDTO';
import { InvoiceItemViewDTO } from './invoiceItemViewDTO';
import { ItemDTO } from './itemDTO';

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
  createdBy?: string;
  createdDate?: Date;
  lastModifiedBy?: string;
  lastModifiedDate?: Date;
  invoiceItemsCount?: string;
  deferred?: boolean;
}
export namespace InvoiceViewDTO {
  export type KindEnum = 'SALE' | 'PURCHASE' | 'ADJUST' | 'TRANSFER' | 'SALEQUOTE' | 'RETURNPUR' | 'RETURNSALE' | 'COMPONENT';
  export const KindEnum = {
    SALE: 'SALE' as KindEnum,
    PURCHASE: 'PURCHASE' as KindEnum,
    ADJUST: 'ADJUST' as KindEnum,
    TRANSFER: 'TRANSFER' as KindEnum,
    SALEQUOTE: 'SALEQUOTE' as KindEnum,
    RETURNPUR: 'RETURNPUR' as KindEnum,
    RETURNSALE: 'RETURNSALE' as KindEnum,
    COMPONENT: 'COMPONENT' as KindEnum,
  };
}
