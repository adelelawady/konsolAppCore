import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'appCurrency',
})
export class AppCurrencyPipe implements PipeTransform {
  public defaultCurrency = 'USD';

  transform(value: number | string | null | undefined, currency?: string): string {
    if (value === null || value === undefined) {
      return '0.00 ' + (currency || this.defaultCurrency);
    }

    const numValue = typeof value === 'string' ? parseFloat(value) : value;
    return `${numValue.toFixed(2)} ${currency || this.defaultCurrency}`;
  }
}
