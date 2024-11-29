import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class CurrencyConfigService {
  private currentCurrency = new BehaviorSubject<string>('$');

  setCurrency(currency: string): void {
    this.currentCurrency.next(currency);
  }

  getCurrency(): Observable<string> {
    return this.currentCurrency.asObservable();
  }

  getCurrentCurrencyValue(): string {
    return this.currentCurrency.value;
  }
}
