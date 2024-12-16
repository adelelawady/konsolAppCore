import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { PsDeviceDTO } from 'app/core/konsolApi/model/psDeviceDTO';

@Injectable({
  providedIn: 'root'
})
export class PlaystationService {
  private selectedDeviceSubject = new BehaviorSubject<PsDeviceDTO | null>(null);
  selectedDevice$ = this.selectedDeviceSubject.asObservable();

  private showOrdersSubject = new BehaviorSubject<boolean>(false);
  showOrders$ = this.showOrdersSubject.asObservable();

  private checkoutSubject = new BehaviorSubject<boolean>(false);
  checkout$ = this.checkoutSubject.asObservable();

  private reloadDevicesSubject = new Subject<void>();
  reloadDevices$ = this.reloadDevicesSubject.asObservable();

  private orderChangeSubject = new Subject<void>();
  orderChange$ = this.orderChangeSubject.asObservable();

  constructor() {}

  selectDevice(device: PsDeviceDTO | null): void {
    this.selectedDeviceSubject.next(device);
  }

  getSelectedDevice(): PsDeviceDTO | null {
    return this.selectedDeviceSubject.value;
  }

  showOrdersList(): void {
    this.showOrdersSubject.next(true);
  }

  hideOrders(): void {
    this.showOrdersSubject.next(false);
  }

  showCheckout(): void {
    this.checkoutSubject.next(true);
  }

  hideCheckout(): void {
    this.checkoutSubject.next(false);
  }

  reloadDevices(): void {
    this.reloadDevicesSubject.next();
  }

  notifyOrderChange(): void {
    this.orderChangeSubject.next();
  }
}
