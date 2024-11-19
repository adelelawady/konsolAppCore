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
import { GlobalSettingsItemsOptions } from './globalSettingsItemsOptions';
import { GlobalSettingsPurchaseInvoiceOptions } from './globalSettingsPurchaseInvoiceOptions';
import { GlobalSettingsSalesInvoiceOptions } from './globalSettingsSalesInvoiceOptions';

export interface GlobalSettings {
  salesInvoiceOptions?: GlobalSettingsSalesInvoiceOptions;
  /**
   * selected folder for report files
   */
  reportFilesDir?: string;
  /**
   * selected store to add and subtract items qty from after saving invoice BE_OPTION
   */
  selectedStoreId?: string;
  /**
   * selected printer name to send print file to UI_OPTION
   */
  mainPrinterName?: string;
  /**
   * culture language for currency persentation in ui / table / print / preview
   */
  cultureName?: string;
  itemsOptions?: GlobalSettingsItemsOptions;
  purchaseInvoiceOptions?: GlobalSettingsPurchaseInvoiceOptions;
  reportPageNamesList?: Array<GlobalSettings.ReportPageNamesListEnum>;
}
export namespace GlobalSettings {
  export type ReportPageNamesListEnum = 'sale' | 'pur' | 'items' | 'invoiceReport';
  export const ReportPageNamesListEnum = {
    Sale: 'sale' as ReportPageNamesListEnum,
    Pur: 'pur' as ReportPageNamesListEnum,
    Items: 'items' as ReportPageNamesListEnum,
    InvoiceReport: 'invoiceReport' as ReportPageNamesListEnum,
  };
}
