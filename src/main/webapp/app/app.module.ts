import { NgModule, LOCALE_ID, DEFAULT_CURRENCY_CODE, Pipe } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import locale from '@angular/common/locales/en';
import { BrowserModule, Title } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ServiceWorkerModule } from '@angular/service-worker';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { NgxWebstorageModule } from 'ngx-webstorage';
import dayjs from 'dayjs/esm';
import { NgbDateAdapter, NgbDatepickerConfig } from '@ng-bootstrap/ng-bootstrap';
import { ToastrModule } from 'ngx-toastr';
import { CurrencyPipe } from '@angular/common';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import './config/dayjs';
import { SharedModule } from 'app/shared/shared.module';
import { TranslationModule } from 'app/shared/language/translation.module';
import { AppRoutingModule } from './app-routing.module';
import { HomeModule } from './home/home.module';
import { NgbDateDayjsAdapter } from './config/datepicker-adapter';
import { fontAwesomeIcons } from './config/font-awesome-icons';
import { httpInterceptorProviders } from 'app/core/interceptor/index';
import { FindLanguageFromKeyPipe } from 'app/shared/language/find-language-from-key.pipe';
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';
import { ApiModule, Configuration } from './core/konsolApi';
import { CurrencyConfigService } from './core/config/currency-config.service';
import { PagesModule } from './pages/pages.module';
import { TranslateModule } from '@ngx-translate/core';
import { LayoutsModule } from './layouts/layouts.module';

import { TranslateDirective } from './shared/language/translate.directive';

@Pipe({
  name: 'customCurrency',
  pure: true
})
export class CustomCurrencyPipe extends CurrencyPipe {
  override transform(value: string | number, currencyCode?: string, display?: string | boolean, digitsInfo?: string, locale?: string): string | null;
  override transform(value: null | undefined, currencyCode?: string, display?: string | boolean, digitsInfo?: string, locale?: string): null;
  override transform(
    value: string | number | null | undefined,
    currencyCode?: string,
    display?: string | boolean,
    digitsInfo?: string,
    locale?: string
  ): string | null {
    const currencyStr = super.transform(value as any, currencyCode || 'EGP', display || 'symbol', digitsInfo || '1.2-2', locale);
    if (!currencyStr) return null;
    // Add space between currency code and number
    return currencyStr.replace(/^([A-Z]{3})(\d)/, '$1 $2');
  }
}

@NgModule({
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    ApiModule.forRoot(
      () =>
        new Configuration({
          basePath: 'api',
        })
    ),
    TranslateModule,
    TranslationModule,
    PagesModule,
    SharedModule,
    HomeModule,
    AppRoutingModule,
    ServiceWorkerModule.register('ngsw-worker.js', { enabled: false }),
    HttpClientModule,
    NgxWebstorageModule.forRoot({ prefix: 'jhi', separator: '-', caseSensitive: true }),
    ToastrModule.forRoot({
      positionClass: 'toast-top-right',
      preventDuplicates: true,
    }),
    LayoutsModule,
  ],
  exports: [CustomCurrencyPipe],
  providers: [
    Title,
    { provide: LOCALE_ID, useValue: 'en' },
    { provide: DEFAULT_CURRENCY_CODE, useValue: 'EGP' },
    CustomCurrencyPipe,
    { provide: CurrencyPipe, useClass: CustomCurrencyPipe },
    { provide: NgbDateAdapter, useClass: NgbDateDayjsAdapter },
    httpInterceptorProviders,
    //CurrencyConfigService,
    FindLanguageFromKeyPipe,
  ],
  declarations: [CustomCurrencyPipe,MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective],
  bootstrap: [MainComponent],
})
export class AppModule {
  constructor(applicationConfigService: ApplicationConfigService, iconLibrary: FaIconLibrary) {
    registerLocaleData(locale);
    iconLibrary.addIcons(...fontAwesomeIcons);
  }
}
