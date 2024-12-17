import { DEFAULT_CURRENCY_CODE, NgModule } from '@angular/core';
import { CurrencyPipe } from '@angular/common';

import { SharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { TranslateDirective } from './language/translate.directive';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { DurationPipe } from './date/duration.pipe';
import { FormatMediumDatetimePipe } from './date/format-medium-datetime.pipe';
import { FormatMediumDatePipe } from './date/format-medium-date.pipe';
import { SortByDirective } from './sort/sort-by.directive';
import { SortDirective } from './sort/sort.directive';
import { ItemCountComponent } from './pagination/item-count.component';
import { FilterComponent } from './filter/filter.component';
import { ItemsSearchBoxComponent } from './components/items-search-box/items-search-box.component';
import { AppCurrencyPipe } from './pipes/app-currency.pipe';
import { DataTableComponent } from './components/data-table/data-table.component';
import { PosInvoiceComponent } from './components/pos-invoice/pos-invoice.component';
import { StoreSelectorComponent } from './components/store-selector/store-selector.component';
import { BankSelectorComponent } from './components/bank-selector/bank-selector.component';
import { AccountSelectorComponent } from './components/account-selector/account-selector.component';
import { TranslationModule } from '../shared/language/translation.module';
import { CurrencySpacePipe } from './pipes/currency-space.pipe';

@NgModule({
  imports: [SharedLibsModule],
  declarations: [
    AlertComponent,
    FindLanguageFromKeyPipe,
    TranslateDirective,
    AlertErrorComponent,
    HasAnyAuthorityDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    SortByDirective,
    SortDirective,
    ItemCountComponent,
    FilterComponent,
    ItemsSearchBoxComponent,
    AppCurrencyPipe,
    DataTableComponent,
    PosInvoiceComponent,
    StoreSelectorComponent,
    BankSelectorComponent,
    AccountSelectorComponent,
    CurrencySpacePipe
  ],
  exports: [
    SharedLibsModule,
    FindLanguageFromKeyPipe,
    TranslateDirective,
    AlertComponent,
    AlertErrorComponent,
    HasAnyAuthorityDirective,
    DurationPipe,
    FormatMediumDatetimePipe,
    FormatMediumDatePipe,
    SortByDirective,
    SortDirective,
    ItemCountComponent,
    FilterComponent,
    ItemsSearchBoxComponent,
    AppCurrencyPipe,
    DataTableComponent,
    PosInvoiceComponent,
    StoreSelectorComponent,
    BankSelectorComponent,
    AccountSelectorComponent,
    CurrencySpacePipe
  ],
  providers: [
    AppCurrencyPipe,
    CurrencyPipe,
    { provide: DEFAULT_CURRENCY_CODE, useValue: 'EGP' }, // Replace 'USD' with your desired default currency code
  ],
})
export class SharedModule {}
