export * from './accountResource.service';
import { AccountResourceService } from './accountResource.service';
export * from './accountUserResource.service';
import { AccountUserResourceService } from './accountUserResource.service';
export * from './bankResource.service';
import { BankResourceService } from './bankResource.service';
export * from './financialReports.service';
import { FinancialReportsService } from './financialReports.service';
export * from './gLOBAL.service';
import { GLOBALService } from './gLOBAL.service';
export * from './invoiceResource.service';
import { InvoiceResourceService } from './invoiceResource.service';
export * from './itemResource.service';
import { ItemResourceService } from './itemResource.service';
export * from './moneyResource.service';
import { MoneyResourceService } from './moneyResource.service';
export * from './playstationResource.service';
import { PlaystationResourceService } from './playstationResource.service';
export * from './publicUserResource.service';
import { PublicUserResourceService } from './publicUserResource.service';
export * from './serverSettings.service';
import { ServerSettingsService } from './serverSettings.service';
export * from './storeResource.service';
import { StoreResourceService } from './storeResource.service';
export * from './userJwtController.service';
import { UserJwtControllerService } from './userJwtController.service';
export * from './userResource.service';
import { UserResourceService } from './userResource.service';
export const APIS = [
  AccountResourceService,
  AccountUserResourceService,
  BankResourceService,
  FinancialReportsService,
  GLOBALService,
  InvoiceResourceService,
  ItemResourceService,
  MoneyResourceService,
  PlaystationResourceService,
  PublicUserResourceService,
  ServerSettingsService,
  StoreResourceService,
  UserJwtControllerService,
  UserResourceService,
];
