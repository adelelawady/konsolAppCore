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
import { LicenseDTO } from './licenseDTO';

export interface ServerSettings {
  id?: string;
  /**
   * License information for the server
   */
  license?: Array<LicenseDTO>;
  MAIN_SELECTED_STORE_ID?: string;
  MAIN_SELECTED_BANK_ID?: string;
  SALES_CHECK_ITEM_QTY?: boolean;
  SALES_UPDATE_ITEM_QTY_AFTER_SAVE?: boolean;
  PURCHASE_UPDATE_ITEM_QTY_AFTER_SAVE?: boolean;
  PLAYSTATION_SELECTED_STORE_ID?: string;
  PLAYSTATION_SELECTED_BANK_ID?: string;
  /**
   * Indicates that inventory can drop below zero.
   */
  ALLOW_NEGATIVE_INVENTORY?: boolean;
  /**
   * This option determines whether deleted invoice items should be saved or logged for record-keeping purposes. When enabled, any items removed from an invoice are retained in the system, allowing for auditing or restoration if needed.
   */
  SAVE_INVOICE_DELETETED_INVOICEITEMS?: boolean;
  /**
   * Enable/disable automatic backup system
   */
  BACKUP_ENABLED?: boolean;
  /**
   * Frequency of automatic backups
   */
  BACKUP_SCHEDULE_TYPE?: ServerSettings.BACKUPSCHEDULETYPEEnum;
  /**
   * Time to run backup (HH:mm format)
   */
  BACKUP_TIME?: string;
  /**
   * Days to run backup (for WEEKLY schedule)
   */
  BACKUP_DAYS?: Array<ServerSettings.BACKUPDAYSEnum>;
  /**
   * Number of days to keep backup files
   */
  BACKUP_RETENTION_DAYS?: number;
  /**
   * Directory path to store backup files
   */
  BACKUP_LOCATION?: string;
  /**
   * Include file attachments in backup
   */
  BACKUP_INCLUDE_FILES?: boolean;
  /**
   * Compress backup files
   */
  BACKUP_COMPRESS?: boolean;
  /**
   * Path to MongoDB dump executable
   */
  MONGODB_DUMP_PATH?: string;
  /**
   * Path to MongoDB restore executable
   */
  MONGODB_RESTORE_PATH?: string;
}
export namespace ServerSettings {
  export type BACKUPSCHEDULETYPEEnum = 'DAILY' | 'WEEKLY' | 'MONTHLY' | 'MANUAL';
  export const BACKUPSCHEDULETYPEEnum = {
    Daily: 'DAILY' as BACKUPSCHEDULETYPEEnum,
    Weekly: 'WEEKLY' as BACKUPSCHEDULETYPEEnum,
    Monthly: 'MONTHLY' as BACKUPSCHEDULETYPEEnum,
    Manual: 'MANUAL' as BACKUPSCHEDULETYPEEnum,
  };
  export type BACKUPDAYSEnum = 'MONDAY' | 'TUESDAY' | 'WEDNESDAY' | 'THURSDAY' | 'FRIDAY' | 'SATURDAY' | 'SUNDAY';
  export const BACKUPDAYSEnum = {
    Monday: 'MONDAY' as BACKUPDAYSEnum,
    Tuesday: 'TUESDAY' as BACKUPDAYSEnum,
    Wednesday: 'WEDNESDAY' as BACKUPDAYSEnum,
    Thursday: 'THURSDAY' as BACKUPDAYSEnum,
    Friday: 'FRIDAY' as BACKUPDAYSEnum,
    Saturday: 'SATURDAY' as BACKUPDAYSEnum,
    Sunday: 'SUNDAY' as BACKUPDAYSEnum,
  };
}
