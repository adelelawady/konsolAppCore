import { NgModule, ModuleWithProviders, SkipSelf, Optional } from '@angular/core';
import { Configuration } from './configuration';
import { HttpClient } from '@angular/common/http';

import { AccountResourceService } from './api/accountResource.service';
import { AccountUserResourceService } from './api/accountUserResource.service';
import { BankResourceService } from './api/bankResource.service';
import { FinancialReportsService } from './api/financialReports.service';
import { GLOBALService } from './api/gLOBAL.service';
import { InvoiceResourceService } from './api/invoiceResource.service';
import { ItemResourceService } from './api/itemResource.service';
import { MoneyResourceService } from './api/moneyResource.service';
import { PlaystationResourceService } from './api/playstationResource.service';
import { PublicUserResourceService } from './api/publicUserResource.service';
import { ServerSettingsService } from './api/serverSettings.service';
import { StoreResourceService } from './api/storeResource.service';
import { UserJwtControllerService } from './api/userJwtController.service';
import { UserResourceService } from './api/userResource.service';

@NgModule({
  imports: [],
  declarations: [],
  exports: [],
  providers: [],
})
export class ApiModule {
  public static forRoot(configurationFactory: () => Configuration): ModuleWithProviders<ApiModule> {
    return {
      ngModule: ApiModule,
      providers: [{ provide: Configuration, useFactory: configurationFactory }],
    };
  }

  constructor(@Optional() @SkipSelf() parentModule: ApiModule, @Optional() http: HttpClient) {
    if (parentModule) {
      throw new Error('ApiModule is already loaded. Import in your base AppModule only.');
    }
    if (!http) {
      throw new Error(
        'You need to import the HttpClientModule in your AppModule! \n' + 'See also https://github.com/angular/angular/issues/20575'
      );
    }
  }
}
