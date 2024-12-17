import { Pipe, PipeTransform } from '@angular/core';
import { CurrencyPipe } from '@angular/common';

@Pipe({
  name: 'currencyK'
})
export class CurrencySpacePipe implements PipeTransform {
     transform(value: string | number | null | undefined, currencyCode?: string, display?: string | boolean, digitsInfo?: string, locale?: string): string | null {
      if (value === null || value === undefined) return null;
      
      const currencyPipe = new CurrencyPipe(locale || 'en');
      const currencyStr = currencyPipe.transform(value, currencyCode || 'EGP', display || 'symbol', digitsInfo || '1.2-2');
      if (!currencyStr) return null;
      
      // Add space between currency code and number
      return currencyStr.replace(/^([A-Z]{3})(\d)/, '$1 $2');
    }
} 

