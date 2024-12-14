import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Device } from '../components/device-card/device-card.component';

@Injectable({
  providedIn: 'root'
})
export class PlaystationService {
  private selectedDeviceSubject = new BehaviorSubject<Device | null>(null);
  private ordersListVisibleSubject = new BehaviorSubject<boolean>(false);

  selectedDevice$ = this.selectedDeviceSubject.asObservable();
  ordersListVisible$ = this.ordersListVisibleSubject.asObservable();

  constructor() {}

  selectDevice(device: Device): void {
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
