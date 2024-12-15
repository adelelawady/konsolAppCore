import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { PsDeviceDTO } from 'app/core/konsolApi';

@Injectable({
  providedIn: 'root'
})
export class PlaystationService {
  private selectedDeviceSubject = new BehaviorSubject<PsDeviceDTO | null>(null);
  private ordersListVisibleSubject = new BehaviorSubject<boolean>(false);

  selectedDevice$ = this.selectedDeviceSubject.asObservable();
  ordersListVisible$ = this.ordersListVisibleSubject.asObservable();

  constructor() {}

  selectDevice(device: PsDeviceDTO): void {
    this.selectedDeviceSubject.next(device);
  }

  clearSelectedDevice(): void {
    this.selectedDeviceSubject.next(null);
  }

  showOrdersList(): void {
    this.ordersListVisibleSubject.next(true);
  }

  hideOrdersList(): void {
    this.ordersListVisibleSubject.next(false);
  }

  toggleOrdersList(): void {
    this.ordersListVisibleSubject.next(!this.ordersListVisibleSubject.value);
  }
}
