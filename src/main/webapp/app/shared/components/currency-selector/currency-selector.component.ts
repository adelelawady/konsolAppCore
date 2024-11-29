import { Component, OnInit } from '@angular/core';
import { CurrencyConfigService } from 'app/core/config/currency-config.service';

@Component({
  selector: 'jhi-currency-selector',
  template: `
    <select class="form-select" [(ngModel)]="selectedCurrency" (change)="onCurrencyChange()">
      <option value="د.ل">د.ل</option>
      <option value="$">$</option>
      <option value="€">€</option>
    </select>
  `,
})
export class CurrencySelectorComponent implements OnInit {
  selectedCurrency: string;

  constructor(private currencyConfig: CurrencyConfigService) {
    this.selectedCurrency = this.currencyConfig.getCurrentCurrencyValue();
  }

  ngOnInit(): void {
    this.currencyConfig.getCurrency().subscribe(currency => {
      this.selectedCurrency = currency;
    });
  }

  onCurrencyChange(): void {
    this.currencyConfig.setCurrency(this.selectedCurrency);
  }
}
